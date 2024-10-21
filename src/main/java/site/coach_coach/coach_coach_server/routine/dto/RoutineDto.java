package site.coach_coach.coach_coach_server.routine.dto;

import java.util.List;
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
	Boolean isCompleted,

	List<ActionDto> actions

) {
	public static RoutineDto from(Routine routine) {
		List<ActionDto> actions = routine.getActions().stream()
			.map(ActionDto::from)
			.collect(Collectors.toList());

		return new RoutineDto(
			routine.getRoutineId(),
			routine.getRoutineName(),
			routine.getSport().getSportName(),
			routine.getIsCompleted(),
			actions
		);
	}
}
