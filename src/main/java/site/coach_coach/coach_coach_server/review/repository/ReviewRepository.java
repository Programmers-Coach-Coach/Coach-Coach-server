package site.coach_coach.coach_coach_server.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByCoach_CoachId(Long coachId);
}
