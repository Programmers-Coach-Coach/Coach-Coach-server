package site.coach_coach.coach_coach_server.like.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.like.domain.UserCoachLike;

public interface UserCoachLikeRepository extends JpaRepository<UserCoachLike, Long> {
	int countByCoach_CoachId(Long coachId);

	boolean existsByUser_UserIdAndCoach_CoachId(Long userId, Long coachId);

	void deleteByUser_UserIdAndCoach_CoachId(Long userId, Long coachId);

	@Query("SELECT u.coach FROM UserCoachLike u "
		+ "WHERE u.createdAt >= :since AND u.coach.isOpen = true GROUP BY u.coach ORDER BY COUNT(u) DESC")
	List<Coach> findTopCoachesByLikesSinceAndIsOpenTrue(@Param("since") LocalDateTime since, Pageable pageable);
}
