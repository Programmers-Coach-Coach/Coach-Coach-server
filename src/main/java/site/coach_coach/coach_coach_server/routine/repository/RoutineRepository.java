package site.coach_coach.coach_coach_server.routine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.routine.domain.Routine;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
	List<Routine> findByUserIdAndCoachIdIsNull(Long userId);

	List<Routine> findByUserIdAndCoachId(Long userId, Long coachId);
}
