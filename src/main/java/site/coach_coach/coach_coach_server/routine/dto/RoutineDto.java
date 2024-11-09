package site.coach_coach.coach_coach_server.routine.dto;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import site.coach_coach.coach_coach_server.action.dto.ActionDto;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

@Builder
public record RoutineDto(
	@NotNull
	Long routineId,

	@NotNull
	String routineName,

	@NotNull
	String sportName,

	@NotNull
	Set<DayOfWeek> repeats,

	@NotNull
	Boolean isCompleted,

	List<ActionDto> actions

) {
	public static RoutineDto convertToDtoWithoutNullAction(Routine routine) {
		List<ActionDto> actions = routine.getActions().stream()
			.filter(action -> action.getActionName() != null)
			.map(ActionDto::from)
			.collect(Collectors.toList());

		return new RoutineDto(
			routine.getRoutineId(),
			routine.getRoutineName(),
			routine.getSport().getSportName(),
			routine.getRepeats(),
			routine.getIsCompleted(),
			actions
		);
	}
}
