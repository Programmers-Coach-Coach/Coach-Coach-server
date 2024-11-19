package site.coach_coach.coach_coach_server.coach.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.chat.dto.request.ChatRoomRequest;
import site.coach_coach.coach_coach_server.chat.service.ChatRoomService;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.coach.dto.CoachDetailDto;
import site.coach_coach.coach_coach_server.coach.dto.CoachListDto;
import site.coach_coach.coach_coach_server.coach.dto.CoachListResponse;
import site.coach_coach.coach_coach_server.coach.dto.CoachRequest;
import site.coach_coach.coach_coach_server.coach.dto.MatchingCoachResponseDto;
import site.coach_coach.coach_coach_server.coach.dto.MatchingUserResponseDto;
import site.coach_coach.coach_coach_server.coach.dto.ReviewListDto;
import site.coach_coach.coach_coach_server.coach.dto.ReviewRequestDto;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;
import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.common.exception.DuplicateValueException;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;
import site.coach_coach.coach_coach_server.common.exception.SelfRequestNotAllowedException;
import site.coach_coach.coach_coach_server.common.exception.UserNotFoundException;
import site.coach_coach.coach_coach_server.like.domain.UserCoachLike;
import site.coach_coach.coach_coach_server.like.repository.UserCoachLikeRepository;
import site.coach_coach.coach_coach_server.matching.domain.Matching;
import site.coach_coach.coach_coach_server.matching.repository.MatchingRepository;
import site.coach_coach.coach_coach_server.notification.service.NotificationService;
import site.coach_coach.coach_coach_server.review.domain.Review;
import site.coach_coach.coach_coach_server.review.dto.ReviewDto;
import site.coach_coach.coach_coach_server.review.dto.ReviewSortOption;
import site.coach_coach.coach_coach_server.review.repository.ReviewRepository;
import site.coach_coach.coach_coach_server.sport.domain.CoachingSport;
import site.coach_coach.coach_coach_server.sport.domain.Sport;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;
import site.coach_coach.coach_coach_server.sport.dto.InterestedSportDto;
import site.coach_coach.coach_coach_server.sport.repository.CoachingSportRepository;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CoachService {

	private final CoachRepository coachRepository;
	private final ReviewRepository reviewRepository;
	private final UserCoachLikeRepository userCoachLikeRepository;
	private final CoachingSportRepository coachingSportRepository;
	private final MatchingRepository matchingRepository;
	private final NotificationService notificationService;
	private final SportRepository sportRepository;
	private final UserRepository userRepository;
	private final ChatRoomService chatRoomService;

	@Transactional(readOnly = true)
	public Coach getCoachByUserId(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		return coachRepository.findByUser(user)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));
	}

	@Transactional
	public Coach createCoachForUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		user.promoteToCoach();
		userRepository.save(user);
		return coachRepository.save(new Coach(user));
	}

	@Transactional
	public void updateCoachInfo(Coach coach, CoachRequest coachRequest) {
		coach.update(
			coachRequest.coachIntroduction(),
			coachRequest.activeCenter() != null ? coachRequest.activeCenter() : coach.getActiveCenter(),
			coachRequest.activeCenterDetail() != null ? coachRequest.activeCenterDetail() :
				coach.getActiveCenterDetail(),
			coachRequest.activeHours(),
			coachRequest.chattingUrl(),
			coachRequest.isOpen() != null ? coachRequest.isOpen() : coach.getIsOpen()
		);

		coachRepository.save(coach);
	}

	@Transactional
	public void addNewCoachingSports(Coach coach, List<CoachingSportDto> coachingSports) {
		List<Sport> sports = coachingSports.stream()
			.map(coachingSportDto -> sportRepository.findBySportName(coachingSportDto.sportName())
				.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_SPORTS)))
			.toList();

		List<CoachingSport> coachingSportsEntities = sports.stream()
			.map(sport -> new CoachingSport(coach, sport))
			.collect(Collectors.toList());

		coachingSportRepository.saveAll(coachingSportsEntities);
	}

	@Transactional
	public void saveOrUpdateCoach(Long userId, CoachRequest coachRequest) {
		Coach coach;
		try {
			coach = getCoachByUserId(userId);
		} catch (NotFoundException e) {
			coach = createCoachForUser(userId);
		}
		updateCoachInfo(coach, coachRequest);
		removeExistingCoachingSports(coach);
		addNewCoachingSports(coach, coachRequest.coachingSports());
	}

	@Transactional
	public void removeExistingCoachingSports(Coach coach) {
		coachingSportRepository.deleteByCoach(coach);
	}

	@Transactional
	public void contactCoach(User user, Long coachId) {
		Coach coach = coachRepository.findById(coachId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));

		if (user.getUserId().equals(coach.getUser().getUserId())) {
			throw new SelfRequestNotAllowedException(ErrorMessage.CANNOT_CONTACT_SELF);
		}

		if (matchingRepository.existsByUserUserIdAndCoachCoachId(user.getUserId(), coachId)) {
			throw new DuplicateValueException(ErrorMessage.DUPLICATE_CONTACT);
		}

		Matching newMatching = new Matching(null, user, coach, false);
		matchingRepository.save(newMatching);

		ChatRoomRequest chatRoomRequest = new ChatRoomRequest(user.getUserId(), coach.getCoachId(),
			newMatching.getUserCoachMatchingId());
		Long chatRoomId = chatRoomService.createChatRoom(chatRoomRequest);

		notificationService.createNotification(user.getUserId(), coachId, RelationFunctionEnum.ask);
	}

	@Transactional
	public void deleteMatching(Long coachUserId, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		Coach coach = coachRepository.findByUser_UserId(coachUserId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));

		Matching matching = matchingRepository.findByUser_UserIdAndCoach_CoachId(user.getUserId(), coach.getCoachId())
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CONTACT));

		boolean isMatching = matching.getIsMatching();
		matchingRepository.delete(matching);
		if (isMatching) {
			notificationService.createNotification(userId, coach.getCoachId(), RelationFunctionEnum.cancel);
		} else {
			notificationService.createNotification(userId, coach.getCoachId(), RelationFunctionEnum.refusal);
		}
	}

	@Transactional(readOnly = true)
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public Coach getCoachById(Long coachId) {
		return coachRepository.findById(coachId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));
	}

	@Transactional(readOnly = true)
	public CoachDetailDto getCoachDetail(User user, Long coachId) {
		Coach coach = (coachId != null) ? getCoachById(coachId) : getCoachByUserId(user.getUserId());

		boolean isSelf = user.getUserId().equals(coach.getUser().getUserId());

		if (!isSelf && !coach.getIsOpen()) {
			throw new AccessDeniedException();
		}

		List<ReviewDto> reviews = getReviews(coach, user, ReviewSortOption.LATEST);
		double averageRating = calculateAverageRating(reviews);

		boolean isLiked = isLikedByUser(user, coach);
		boolean isContacted = matchingRepository.existsByUserUserIdAndCoachCoachId(user.getUserId(), coachId);

		List<CoachingSportDto> coachingSports = getCoachingSports(coach);
		boolean isMatched = matchingRepository.existsByUserUserIdAndCoachCoachIdAndIsMatching(
			user.getUserId(), coach.getCoachId(), true);

		return CoachDetailDto.builder()
			.coachId(coach.getCoachId())
			.coachName(coach.getUser().getNickname())
			.coachGender(coach.getUser().getGender())
			.localAddress(coach.getUser().getLocalAddress())
			.profileImageUrl(coach.getUser().getProfileImageUrl())
			.coachIntroduction(coach.getCoachIntroduction())
			.coachingSports(coachingSports)
			.activeCenter(coach.getActiveCenter())
			.activeCenterDetail(coach.getActiveCenterDetail())
			.activeHours(coach.getActiveHours())
			.chattingUrl(coach.getChattingUrl())
			.isOpen(coach.getIsOpen())
			.isContacted(isContacted)
			.isMatched(isMatched)
			.isLiked(isLiked)
			.isSelf(isSelf)
			.reviewRating(averageRating)
			.totalUserCount(coach.getTotalUserCount())
			.build();
	}

	@Transactional(readOnly = true)
	public CoachListResponse getAllCoaches(User user, int page, String sports, String search, String gender,
		Boolean latest, Boolean review, Boolean liked, Boolean my) {

		List<Long> allSportsIds = sportRepository.findAllSportIds();

		Sort sort = Sort.by(Sort.Order.desc("updatedAt"));
		Pageable pageable = PageRequest.of(page - 1, 20, sort);

		List<Long> sportsList = (sports != null && !sports.isEmpty()) ? parseSports(sports) : allSportsIds;
		sportsList = getExistingSportsList(sportsList);

		GenderEnum genderEnum = parseGender(gender);

		if (sportsList.isEmpty() && (sports != null && !sports.isEmpty())) {
			return new CoachListResponse(List.of(), 0, page);
		}

		Page<Coach> coachesPage;
		coachesPage = fetchCoachesPage(user, sportsList, search, genderEnum, pageable, review, liked, latest, my);

		if (coachesPage.isEmpty() && page == 1) {
			return new CoachListResponse(List.of(), 0, page);
		}

		if (page > coachesPage.getTotalPages()) {
			throw new NotFoundException(ErrorMessage.NOT_FOUND_PAGE);
		}

		List<CoachListDto> coaches = coachesPage.getContent().stream()
			.filter(Coach::getIsOpen)
			.map(coach -> getCoachListDto(coach, user))
			.collect(Collectors.toList());

		return new CoachListResponse(coaches, (int)coachesPage.getTotalElements(), page);
	}

	@Transactional
	public void addCoachToFavorites(Long userId, Long coachId) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		Coach coach = coachRepository.findById(coachId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));

		if (userId.equals(coach.getUser().getUserId())) {
			throw new SelfRequestNotAllowedException(ErrorMessage.CANNOT_LIKE_SELF);
		}

		if (!userCoachLikeRepository.existsByUser_UserIdAndCoach_CoachId(userId, coachId)) {
			userCoachLikeRepository.save(new UserCoachLike(null, user, coach));
		}
	}

	@Transactional
	public void deleteCoachToFavorites(Long userId, Long coachId) {
		coachRepository.findById(coachId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));

		if (userCoachLikeRepository.existsByUser_UserIdAndCoach_CoachId(userId, coachId)) {
			userCoachLikeRepository.deleteByUser_UserIdAndCoach_CoachId(userId, coachId);
		}
	}


	public List<MatchingUserResponseDto> getMatchingUsersByCoachId(Long coachUserId) {
		Long coachId = coachRepository.findCoachIdByUserId(coachUserId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));

		List<Matching> matchings = matchingRepository.findByCoach_CoachId(coachId);

		return matchings.stream()
			.map(this::buildMatchingUserResponseDto)
			.collect(Collectors.toList());
	}

	private MatchingUserResponseDto buildMatchingUserResponseDto(Matching matching) {
		User user = matching.getUser();

		List<InterestedSportDto> interestedSports = user.getInterestedSports().stream()
			.map(cs -> new InterestedSportDto(
				cs.getSport().getSportId(),
				cs.getSport().getSportName()
			))
			.collect(Collectors.toList());

		String startDate = matching.getUpdatedAt().toString();

		return new MatchingUserResponseDto(
			user.getUserId(),
			user.getNickname(),
			user.getProfileImageUrl(),
			user.getLocalAddress(),
			interestedSports,
			startDate,
			matching.getIsMatching()
		);
	}


	@Transactional
	public void addReview(Long userId, Long coachId, ReviewRequestDto requestDto) {
		User user = getUserById(userId);
		Coach coach = getCoachById(coachId);

		if (user.getUserId().equals(coach.getUser().getUserId())) {
			throw new SelfRequestNotAllowedException(ErrorMessage.CANNOT_REVIEW_SELF);
		}

		matchingRepository.findByUser_UserIdAndCoach_CoachIdAndIsMatching(userId, coachId, true)
			.orElseThrow(AccessDeniedException::new);

		reviewRepository.findByUser_UserIdAndCoach_CoachId(userId, coachId)
			.ifPresent(review -> {
				throw new DuplicateValueException(ErrorMessage.ALREADY_EXISTS_REVIEW);
			});

		Review review = new Review(null, coach, user, requestDto.contents(), requestDto.stars());

		reviewRepository.save(review);
		notificationService.createNotification(user.getUserId(), coachId, RelationFunctionEnum.review);
	}

	@Transactional
	public void updateReview(Long userId, Long reviewId, ReviewRequestDto requestDto) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_REVIEW));

		if (!review.getUser().getUserId().equals(userId)) {
			throw new AccessDeniedException();
		}

		review.updateContents(requestDto.contents(), requestDto.stars());
		reviewRepository.save(review);
	}

	@Transactional
	public void deleteReview(Long reviewId, Long userId) {
		Review review = reviewRepository.findByReviewIdAndUser_UserId(reviewId, userId)
			.orElseThrow(AccessDeniedException::new);

		reviewRepository.delete(review);
	}

	public List<MatchingCoachResponseDto> getMatchingCoachesByUserId(Long userId) {
		List<Matching> matchings = matchingRepository.findByUser_UserId(userId);
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		return matchings.stream()
			.map(matching -> buildMatchingCoachResponseDto(matching, user))
			.collect(Collectors.toList());
	}

	private MatchingCoachResponseDto buildMatchingCoachResponseDto(Matching matching, User user) {
		Coach coach = matching.getCoach();

		boolean isLiked = isLikedByUser(user, coach);

		List<CoachingSportDto> coachingSports = coach.getCoachingSports().stream()
			.map(cs -> new CoachingSportDto(
				cs.getSport().getSportId(),
				cs.getSport().getSportName()
			))
			.collect(Collectors.toList());

		return new MatchingCoachResponseDto(
			coach.getCoachId(),
			coach.getUser().getNickname(),
			coach.getUser().getProfileImageUrl(),
			coach.getUser().getLocalAddress(),
			coachingSports,
			isLiked,
			matching.getIsMatching()
		);
	}

	private List<Long> getExistingSportsList(List<Long> sportsList) {
		if (sportsList == null || sportsList.isEmpty()) {
			return List.of();
		}

		List<CoachingSport> existingSports = coachingSportRepository.findAllBySport_SportIdIn(sportsList);

		return existingSports.stream()
			.map(cs -> cs.getSport().getSportId())
			.collect(Collectors.toList());
	}

	private Page<Coach> fetchCoachesPage(User user, List<Long> sportsList, String search, GenderEnum genderEnum,
		Pageable pageable, Boolean review, Boolean liked, Boolean latest, Boolean my) {
		Page<Coach> coachesPage;

		if (Boolean.TRUE.equals(review)) {
			coachesPage = coachRepository.findAllWithReviewsSorted(sportsList, search, genderEnum, pageable);
		} else if (Boolean.TRUE.equals(liked)) {
			coachesPage = coachRepository.findAllWithLikesSorted(sportsList, search, genderEnum, pageable);
		} else if (Boolean.TRUE.equals(latest)) {
			coachesPage = coachRepository.findAllWithLatestSorted(sportsList, search, genderEnum, pageable);
		} else if (Boolean.TRUE.equals(my)) {
			coachesPage = coachRepository.findMyCoaches(user.getUserId(), sportsList, search, genderEnum, pageable);
		} else {
			coachesPage = coachRepository.findAllWithLatestSorted(sportsList, search, genderEnum, pageable);
		}

		List<Coach> filteredCoaches = coachesPage.getContent().stream()
			.filter(Coach::getIsOpen)
			.collect(Collectors.toList());

		return new PageImpl<>(filteredCoaches, pageable, coachesPage.getTotalElements());
	}

	private GenderEnum parseGender(String gender) {
		if (gender == null || gender.isEmpty()) {
			return null;
		}
		try {
			return GenderEnum.valueOf(gender.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(ErrorMessage.INVALID_REQUEST);
		}
	}

	private CoachListDto getCoachListDto(Coach coach, User user) {
		List<Review> reviews = reviewRepository.findByCoach_CoachId(coach.getCoachId());
		double averageRating = reviews.stream().mapToInt(Review::getStars).average().orElse(0.0);
		averageRating = Math.round(averageRating * 10.0) / 10.0;
		int countOfReviews = reviews.size();

		boolean isLiked = isLikedByUser(user, coach);
		int countOfLikes = getCountOfLikes(coach);

		List<CoachingSportDto> coachingSports = coach.getCoachingSports().stream()
			.map(cs -> new CoachingSportDto(
				cs.getSport().getSportId(),
				cs.getSport().getSportName()
			))
			.collect(Collectors.toList());

		return CoachListDto.builder()
			.coachId(coach.getCoachId())
			.coachName(coach.getUser().getNickname())
			.localAddress(coach.getUser().getLocalAddress())
			.profileImageUrl(coach.getUser().getProfileImageUrl())
			.coachIntroduction(coach.getCoachIntroduction())
			.coachingSports(coachingSports)
			.countOfReviews(countOfReviews)
			.reviewRating(averageRating)
			.isLiked(isLiked)
			.countOfLikes(countOfLikes)
			.build();
	}

	@Transactional
	public ReviewListDto getAllReviews(User user, Long coachId, ReviewSortOption sortOption) {
		Coach coach = (coachId != null) ? getCoachById(coachId) : getCoachByUserId(user.getUserId());

		boolean isSelf = user.getUserId().equals(coach.getUser().getUserId());

		if (!isSelf && !coach.getIsOpen()) {
			throw new AccessDeniedException();
		}

		List<ReviewDto> reviews = getReviews(coach, user, sortOption);
		double averageRating = calculateAverageRating(reviews);

		boolean isMatched = matchingRepository.existsByUserUserIdAndCoachCoachIdAndIsMatching(
			user.getUserId(), coach.getCoachId(), true);

		return ReviewListDto.builder()
			.reviews(reviews)
			.countOfReviews(reviews.size())
			.reviewRating(averageRating)
			.isMatched(isMatched)
			.isOpen(coach.getIsOpen())
			.build();
	}

	@Transactional(readOnly = true)
	public List<ReviewDto> getReviews(Coach coach, User currentUser, ReviewSortOption sortOption) {
		List<ReviewDto> reviews = reviewRepository.findByCoach_CoachId(coach.getCoachId()).stream()
			.map(review -> new ReviewDto(
				review.getReviewId(),
				review.getUserId(),
				review.getUserNickname(),
				review.getContents(),
				review.getStars(),
				review.getUpdatedAt().toString(),
				Optional.ofNullable(review.getUser())
					.map(user -> user.getUserId().equals(currentUser.getUserId()))
					.orElse(false)
			))
			.collect(Collectors.toList());

		return sortReviews(reviews, sortOption);
	}

	private List<ReviewDto> sortReviews(List<ReviewDto> reviews, ReviewSortOption sortOption) {
		Comparator<ReviewDto> comparator = switch (sortOption) {
			case RATING_ASC -> Comparator.comparing(ReviewDto::stars).reversed()
				.thenComparing(ReviewDto::createdAt).reversed();
			case RATING_DESC -> Comparator.comparing(ReviewDto::stars)
				.thenComparing(ReviewDto::createdAt).reversed();
			case LATEST -> Comparator.comparing(ReviewDto::createdAt).reversed();
		};

		return reviews.stream()
			.sorted(comparator)
			.collect(Collectors.toList());
	}

	public List<CoachingSportDto> getCoachingSports(Coach coach) {
		return coach.getCoachingSports().stream()
			.map(cs -> new CoachingSportDto(
				cs.getSportId(),
				cs.getSportName()
			))
			.collect(Collectors.toList());
	}

	public double calculateAverageRating(List<ReviewDto> reviews) {
		return Math.round(reviews.stream().mapToInt(ReviewDto::stars).average().orElse(0.0) * 10) / 10.0;
	}

	private int getCountOfLikes(Coach coach) {
		return userCoachLikeRepository.countByCoach_CoachId(coach.getCoachId());
	}

	private boolean isLikedByUser(User user, Coach coach) {
		return userCoachLikeRepository.existsByUser_UserIdAndCoach_CoachId(user.getUserId(), coach.getCoachId());
	}

	private List<Long> parseSports(String sports) {
		return Stream.of(sports.split(","))
			.map(Long::parseLong)
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateMatchingStatus(Long coachUserId, Long userId) {
		Coach coach = coachRepository.findByUser_UserId(coachUserId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));

		if (coachUserId.equals(userId)) {
			throw new SelfRequestNotAllowedException(ErrorMessage.CANNOT_MATCHING_SELF);
		}

		Matching matching = matchingRepository.findByUser_UserIdAndCoach_CoachId(userId, coach.getCoachId())
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CONTACT));

		if (Boolean.TRUE.equals(matching.getIsMatching())) {
			throw new DuplicateValueException(ErrorMessage.DUPLICATE_MATCHING);
		}

		matching.markAsMatched();
		coach.increaseTotalUserCount();
		notificationService.createNotification(userId, coach.getCoachId(), RelationFunctionEnum.match);
	}
}
