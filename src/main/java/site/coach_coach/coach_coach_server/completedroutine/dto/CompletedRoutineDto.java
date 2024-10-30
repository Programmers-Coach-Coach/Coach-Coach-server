package site.coach_coach.coach_coach_server.completedroutine.dto;

import java.util.List;
import java.util.stream.Collectors;

import site.coach_coach.coach_coach_server.action.dto.ActionDto;
import site.coach_coach.coach_coach_server.completedroutine.domain.CompletedRoutine;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

public record CompletedRoutineDto(
	Long routineId,
	String routineName,
	List<ActionDto> actions
) {
	public static CompletedRoutineDto from(CompletedRoutine completedRoutine) {
		Routine routine = completedRoutine.getRoutine();
		List<ActionDto> actions = routine.getActions().stream()
			.filter(action -> action.getActionName() != null)
			.map(ActionDto::from)
			.collect(Collectors.toList());

		return new CompletedRoutineDto(
			routine.getRoutineId(),
			routine.getRoutineName(),
			actions
		);
	}
}
