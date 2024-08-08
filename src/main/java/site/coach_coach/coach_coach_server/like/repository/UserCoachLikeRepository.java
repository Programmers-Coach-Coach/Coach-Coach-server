package site.coach_coach.coach_coach_server.like.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import site.coach_coach.coach_coach_server.like.domain.*;

import java.time.*;


//public interface UserCoachLikeRepository extends JpaRepository<UserCoachLike, Long> {
//	@Query("SELECT COUNT(l) FROM UserCoachLike l WHERE l.coach.coachId = :coachId")
//	int countLikesByCoach_CoachId(@Param("coachId") Long coachId);
//
//	@Query("SELECT COUNT(l) FROM UserCoachLike l WHERE l.coach.coachId = :coachId AND l.createdAt >= :startDate")
//	int countRecentLikesByCoach_CoachId(@Param("coachId") Long coachId, @Param("startDate") LocalDateTime startDate);
//
//	boolean existsByUser_UserIdAndCoach_CoachId(Long userId, Long coachId);
//}


public interface UserCoachLikeRepository extends JpaRepository<UserCoachLike, Long> {
	int countLikesByCoach_CoachId(Long coachId);

	@Query("SELECT COUNT(u) FROM UserCoachLike u WHERE u.coach.coachId = :coachId AND u.createdAt >= :startDate")
	int countLikesByCoachAndCreatedAtAfter(@Param("coachId") Long coachId, @Param("startDate") LocalDateTime startDate);

	boolean existsByUser_UserIdAndCoach_CoachId(Long userId, Long coachId);
}
