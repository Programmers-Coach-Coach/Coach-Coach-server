package site.coach_coach.coach_coach_server.action.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.coach_coach.coach_coach_server.action.domain.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
	@Query("SELECT a FROM Action a WHERE a.actionId = :actionId AND a.category.isDeleted IS FALSE "
		+ "AND a.category.routine.routineId IS NOT NULL")
	Optional<Action> checkIsExistAction(@Param("actionId") Long actionId);
}
