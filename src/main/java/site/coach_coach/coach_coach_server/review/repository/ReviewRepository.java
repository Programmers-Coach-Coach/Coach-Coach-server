package site.coach_coach.coach_coach_server.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByCoach_CoachId(Long coachId);

	Optional<Review> findByReviewIdAndUser_UserId(Long reviewId, Long userId);

	Optional<Review> findByUser_UserIdAndCoach_CoachId(Long userId, Long coachId);

}
