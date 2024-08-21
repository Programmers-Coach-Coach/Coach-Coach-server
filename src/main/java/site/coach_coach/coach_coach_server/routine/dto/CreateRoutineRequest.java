package site.coach_coach.coach_coach_server.routine.dto;

import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.common.validation.InputName;

public record CreateRoutineRequest(
	Long userId,

	@NotNull
	Long sportId,

	@NotNull
	@InputName
	String routineName
) {
}
