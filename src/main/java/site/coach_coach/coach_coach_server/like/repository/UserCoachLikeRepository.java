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

	@Query("SELECT COUNT(u) FROM UserCoachLike u WHERE u.coach.coachId = :coachId AND u.createdAt >= :startDate")
	int countLikesByCoachAndCreatedAtAfter(@Param("coachId") Long coachId, @Param("startDate") LocalDateTime startDate);

	boolean existsByUser_UserIdAndCoach_CoachId(Long userId, Long coachId);

	@Query("SELECT u.coach FROM UserCoachLike u WHERE u.createdAt >= :startDate GROUP BY u.coach ORDER BY COUNT(u) DESC")
	List<Coach> findTopCoachesByLikesSince(@Param("startDate") LocalDateTime startDate, Pageable pageable);
}
