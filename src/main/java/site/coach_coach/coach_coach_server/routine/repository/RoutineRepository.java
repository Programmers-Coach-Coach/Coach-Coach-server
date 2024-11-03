package site.coach_coach.coach_coach_server.routine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.routine.domain.Routine;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
	List<Routine> findByUser_UserIdAndCoach_CoachIdIsNull(Long userId);

	List<Routine> findByUser_UserIdAndCoach_CoachId(Long userId, Long coachId);

	@Modifying
	@Query("UPDATE Routine r SET r.isCompleted = FALSE WHERE r.routineId IS NOT NULL")
	void resetIsCompleted();

	@Query("SELECT r FROM Routine r WHERE r.routineId = :routineId AND r.isDeleted IS FALSE ")
	Optional<Routine> findExistRoutine(@Param("routineId") Long routineId);
}
