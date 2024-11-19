package site.coach_coach.coach_coach_server.coach.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.coach.dto.PopularCoachDto;
import site.coach_coach.coach_coach_server.like.repository.UserCoachLikeRepository;
import site.coach_coach.coach_coach_server.review.domain.Review;
import site.coach_coach.coach_coach_server.review.repository.ReviewRepository;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;
import site.coach_coach.coach_coach_server.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopularCoachService {

	private final UserCoachLikeRepository userCoachLikeRepository;
	private final ReviewRepository reviewRepository;

	public List<PopularCoachDto> getTopCoaches(User user) {
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
		List<Coach> topCoaches = userCoachLikeRepository.findTopCoachesByLikesSinceAndIsOpenTrue(
			oneWeekAgo, PageRequest.of(0, 3));

		return topCoaches.stream()
			.map(coach -> createPopularCoachDto(coach, user))
			.collect(Collectors.toList());
	}

	private PopularCoachDto createPopularCoachDto(Coach coach, User user) {
		List<CoachingSportDto> coachingSports = coach.getCoachingSports().stream()
			.map(cs -> new CoachingSportDto(cs.getSport().getSportId(), cs.getSport().getSportName()))
			.collect(Collectors.toList());

		int countOfLikes = userCoachLikeRepository.countByCoach_CoachId(coach.getCoachId());
		boolean isLiked = userCoachLikeRepository
			.existsByUser_UserIdAndCoach_CoachId(user.getUserId(), coach.getCoachId());
		List<Review> reviews = reviewRepository.findByCoach_CoachId(coach.getCoachId());
		double averageRating = reviews.stream().mapToInt(Review::getStars).average().orElse(0.0);

		return new PopularCoachDto(
			coach.getCoachId(),
			coach.getUser().getNickname(),
			coach.getUser().getProfileImageUrl(),
			coach.getCoachIntroduction(),
			countOfLikes,
			averageRating,
			isLiked,
			coachingSports
		);
	}
}
