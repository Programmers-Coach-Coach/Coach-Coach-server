package site.coach_coach.coach_coach_server.action.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.action.domain.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {

	List<Action> findByRoutine_RoutineId(Long routineId);
}
