package site.coach_coach.coach_coach_server.action.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.action.domain.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
	Optional<Action> findByActionIdAndCategory_Routine_RoutineIdIsNotNull(Long actionId);
}
