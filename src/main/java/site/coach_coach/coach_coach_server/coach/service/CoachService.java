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
import site.coach_coach.coach_coach_server.coach.dto.CoachListDto;
import site.coach_coach.coach_coach_server.coach.dto.CoachListResponse;
import site.coach_coach.coach_coach_server.coach.exception.InvalidQueryParameterException;
import site.coach_coach.coach_coach_server.coach.exception.NotFoundPageException;
import site.coach_coach.coach_coach_server.coach.exception.NotFoundSportException;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.like.repository.UserCoachLikeRepository;
import site.coach_coach.coach_coach_server.review.domain.Review;
import site.coach_coach.coach_coach_server.review.repository.ReviewRepository;
import site.coach_coach.coach_coach_server.sport.domain.Sport;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;
import site.coach_coach.coach_coach_server.user.domain.User;

@Service
@RequiredArgsConstructor
public class CoachService {

	private final CoachRepository coachRepository;
	private final ReviewRepository reviewRepository;
	private final UserCoachLikeRepository userCoachLikeRepository;
	private final SportRepository sportRepository;

	@Transactional(readOnly = true)
	public CoachListResponse getAllCoaches(User user, int page, String sports, String search, Boolean latest,
		Boolean review,
		Boolean liked, Boolean my) {

		Sort sort = Sort.unsorted();
		sort = sort.and(Sort.by("updatedAt").descending());

		Pageable pageable = PageRequest.of(page - 1, 20, sort);
		List<Long> sportsList = (sports != null && !sports.isEmpty()) ? parseSports(sports) : null;

		if (sportsList != null && !sportsList.isEmpty()) {
			List<Sport> existingSports = sportRepository.findAllById(sportsList);
			if (existingSports.size() != sportsList.size()) {
				throw new NotFoundSportException(ErrorMessage.NOT_FOUND_SPORTS);
			}
		}

		Page<Coach> coachesPage;
		if (review != null && review) {
			coachesPage = coachRepository.findAllWithReviewsSorted(sportsList, search, pageable);
		} else if (liked != null && liked) {
			coachesPage = coachRepository.findAllWithLikesSorted(sportsList, search, pageable);
		} else if (latest != null && latest) {
			coachesPage = coachRepository.findAllWithLatestSorted(sportsList, search, pageable);
		} else if (my != null && my) {
			coachesPage = coachRepository.findMyCoaches(user.getUserId(), sportsList, search, pageable);
		} else {
			throw new InvalidQueryParameterException(ErrorMessage.INVALID_QUERY_PARAMETER);
		}

		if (page > coachesPage.getTotalPages()) {
			throw new NotFoundPageException(ErrorMessage.NOT_FOUND_PAGE);
		}

		List<CoachListDto> coaches = coachesPage.stream()
			.map(coach -> {
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
			})
			.collect(Collectors.toList());

		return new CoachListResponse(coaches, (int)coachesPage.getTotalElements(), page);
	}

	private int getCountOfLikes(Coach coach) {
		return userCoachLikeRepository.countByCoach_CoachId(coach.getCoachId());
	}

	private boolean isLikedByUser(User user, Coach coach) {
		return userCoachLikeRepository.existsByUser_UserIdAndCoach_CoachId(user.getUserId(), coach.getCoachId());
	}

	private List<Long> parseSports(String sports) {
		if (sports == null || sports.isEmpty()) {
			return List.of();
		}
		return Stream.of(sports.split(","))
			.map(Long::parseLong)
			.collect(Collectors.toList());
	}
}
