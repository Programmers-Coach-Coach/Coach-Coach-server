package site.coach_coach.coach_coach_server.coach.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.coach.dto.CoachDetailDto;
import site.coach_coach.coach_coach_server.coach.dto.CoachListDto;
import site.coach_coach.coach_coach_server.coach.dto.CoachListResponse;
import site.coach_coach.coach_coach_server.coach.dto.CoachRequest;
import site.coach_coach.coach_coach_server.coach.exception.AlreadyMatchedException;
import site.coach_coach.coach_coach_server.coach.exception.DuplicateContactException;
import site.coach_coach.coach_coach_server.coach.exception.NotFoundCoachException;
import site.coach_coach.coach_coach_server.coach.exception.NotFoundMatchingException;
import site.coach_coach.coach_coach_server.coach.exception.NotFoundPageException;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.like.repository.UserCoachLikeRepository;
import site.coach_coach.coach_coach_server.matching.domain.Matching;
import site.coach_coach.coach_coach_server.matching.repository.MatchingRepository;
import site.coach_coach.coach_coach_server.notification.service.NotificationService;
import site.coach_coach.coach_coach_server.review.domain.Review;
import site.coach_coach.coach_coach_server.review.dto.ReviewDto;
import site.coach_coach.coach_coach_server.review.repository.ReviewRepository;
import site.coach_coach.coach_coach_server.sport.domain.CoachingSport;
import site.coach_coach.coach_coach_server.sport.domain.Sport;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;
import site.coach_coach.coach_coach_server.sport.repository.CoachingSportRepository;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
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

	@Transactional(readOnly = true)
	public Coach getCoachByUserId(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
		return coachRepository.findByUser(user)
			.orElseThrow(() -> new NotFoundCoachException(ErrorMessage.NOT_FOUND_COACH));
	}

	@Transactional
	public Coach createCoachForUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
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
		List<Long> sportIds = coachingSports.stream()
			.map(CoachingSportDto::sportId)
			.collect(Collectors.toList());

		List<Sport> sports = sportRepository.findAllById(sportIds);

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
		} catch (NotFoundCoachException e) {
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
			.orElseThrow(() -> new NotFoundCoachException(ErrorMessage.NOT_FOUND_COACH));

		if (matchingRepository.existsByUserUserIdAndCoachCoachId(user.getUserId(), coachId)) {
			throw new DuplicateContactException(ErrorMessage.DUPLICATE_CONTACT);
		}

		Matching newMatching = new Matching(null, user, coach, false);
		matchingRepository.save(newMatching);

		notificationService.createNotification(user.getUserId(), coachId, RelationFunctionEnum.ask);
	}

	@Transactional(readOnly = true)
	public Coach getCoachById(Long coachId) {
		return coachRepository.findById(coachId)
			.orElseThrow(() -> new NotFoundCoachException(ErrorMessage.NOT_FOUND_COACH));
	}

	@Transactional(readOnly = true)
	public Coach getCoachByUserId(User user) {
		return coachRepository.findByUser_UserId(user.getUserId())
			.orElseThrow(AccessDeniedException::new);
	}

	@Transactional(readOnly = true)
	public CoachDetailDto getCoachDetail(User user, Long coachId) {
		Coach coach = (coachId != null) ? getCoachById(coachId) : getCoachByUserId(user);

		List<ReviewDto> reviews = getReviews(coach);
		double averageRating = calculateAverageRating(reviews);

		boolean isLiked = isLikedByUser(user, coach);
		int countOfLikes = getCountOfLikes(coach);

		List<CoachingSportDto> coachingSports = getCoachingSports(coach);

		return CoachDetailDto.builder()
			.coachName(coach.getUser().getNickname())
			.coachGender(coach.getUser().getGender())
			.localAddress(coach.getUser().getLocalAddress())
			.profileImageUrl(coach.getUser().getProfileImageUrl())
			.createdAt(coach.getCreatedAt().toString())
			.coachIntroduction(coach.getCoachIntroduction())
			.coachingSports(coachingSports)
			.activeCenter(coach.getActiveCenter())
			.activeCenterDetail(coach.getActiveCenterDetail())
			.activeHours(coach.getActiveHours())
			.chattingUrl(coach.getChattingUrl())
			.reviews(reviews)
			.isOpen(coach.getIsOpen())
			.countOfReviews(reviews.size())
			.reviewRating(averageRating)
			.isLiked(isLiked)
			.countOfLikes(countOfLikes)
			.build();
	}

	@Transactional(readOnly = true)
	public CoachListResponse getAllCoaches(User user, int page, String sports, String search, Boolean latest,
		Boolean review, Boolean liked, Boolean my) {

		List<Long> allSportsIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);

		Sort sort = Sort.by("updatedAt").descending();
		Pageable pageable = PageRequest.of(page - 1, 20, sort);

		List<Long> sportsList = (sports != null && !sports.isEmpty()) ? parseSports(sports) : allSportsIds;
		sportsList = getExistingSportsList(sportsList);

		if (sportsList.isEmpty() && (sports != null && !sports.isEmpty())) {
			return new CoachListResponse(List.of(), 0, page);
		}

		Page<Coach> coachesPage = fetchCoachesPage(user, sportsList, search, pageable, review, liked, latest, my);

		if (coachesPage.isEmpty() && page == 1) {
			return new CoachListResponse(List.of(), 0, page);
		}

		if (page > coachesPage.getTotalPages()) {
			throw new NotFoundPageException(ErrorMessage.NOT_FOUND_PAGE);
		}

		List<CoachListDto> coaches = coachesPage.stream()
			.map(coach -> getCoachListDto(coach, user))
			.collect(Collectors.toList());

		return new CoachListResponse(coaches, (int)coachesPage.getTotalElements(), page);
	}

	@Transactional
	public void cancelLikeCoach(Long userId, Long coachId) {
		coachRepository.findById(coachId)
			.orElseThrow(() -> new NotFoundCoachException(ErrorMessage.NOT_FOUND_COACH));

		userCoachLikeRepository.deleteByUser_UserIdAndCoach_CoachId(userId, coachId);
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

	private Page<Coach> fetchCoachesPage(User user, List<Long> sportsList, String search, Pageable pageable,
		Boolean review, Boolean liked, Boolean latest, Boolean my) {
		if (Boolean.TRUE.equals(review)) {
			return coachRepository.findAllWithReviewsSorted(sportsList, search, pageable);
		} else if (Boolean.TRUE.equals(liked)) {
			return coachRepository.findAllWithLikesSorted(sportsList, search, pageable);
		} else if (Boolean.TRUE.equals(latest)) {
			return coachRepository.findAllWithLatestSorted(sportsList, search, pageable);
		} else if (Boolean.TRUE.equals(my)) {
			return coachRepository.findMyCoaches(user.getUserId(), sportsList, search, pageable);
		} else {
			return coachRepository.findAllWithLatestSorted(sportsList, search, pageable);
		}
	}

	private CoachListDto getCoachListDto(Coach coach, User user) {
		List<Review> reviews = reviewRepository.findByCoach_CoachId(coach.getCoachId());
		double averageRating = reviews.stream().mapToInt(Review::getStars).average().orElse(0.0);
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

	@Transactional(readOnly = true)
	public List<ReviewDto> getReviews(Coach coach) {
		return reviewRepository.findByCoach_CoachId(coach.getCoachId()).stream()
			.map(review -> new ReviewDto(
				review.getUserId(),
				review.getUserNickname(),
				review.getContents(),
				review.getStars(),
				review.getCreatedAt().toString()
			))
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
		return reviews.stream().mapToInt(ReviewDto::stars).average().orElse(0.0);
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
			.orElseThrow(() -> new NotFoundCoachException(ErrorMessage.NOT_FOUND_COACH));

		Matching matching = matchingRepository.findByUser_UserIdAndCoach_CoachId(userId, coach.getCoachId())
			.orElseThrow(() -> new NotFoundMatchingException(ErrorMessage.NOT_FOUND_MATCHING));

		if (Boolean.TRUE.equals(matching.getIsMatching())) {
			throw new AlreadyMatchedException(ErrorMessage.DUPLICATE_MATCHING);
		}

		matching.markAsMatched();
	}
}
