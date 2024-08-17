package site.coach_coach.coach_coach_server.routine.dto;

import jakarta.validation.constraints.NotNull;

public record CreateRoutineResponse(
	@NotNull
	int statusCode,

	@NotNull
	Long routineId
) {
}
