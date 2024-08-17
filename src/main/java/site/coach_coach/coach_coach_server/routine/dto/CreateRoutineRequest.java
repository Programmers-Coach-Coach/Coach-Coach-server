package site.coach_coach.coach_coach_server.routine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.routine.validation.RoutineName;

public record CreateRoutineRequest(
	Long userId,

	@NotNull
	Long sportId,

	@NotBlank
	@Size(max = 45)
	@RoutineName
	String routineName
) {
}
