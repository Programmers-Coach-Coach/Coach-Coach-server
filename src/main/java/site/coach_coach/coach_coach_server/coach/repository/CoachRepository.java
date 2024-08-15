package site.coach_coach.coach_coach_server.coach.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.coach.domain.Coach;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
	@Query("SELECT c.coachId FROM Coach c WHERE c.user.userId = :userId")
	Optional<Long> findCoachIdByUserId(@Param("userId") Long userId);
}
