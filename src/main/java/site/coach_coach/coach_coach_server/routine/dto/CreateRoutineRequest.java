package site.coach_coach.coach_coach_server.routine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public record CreateRoutineRequest(
	Long userId,

	@NotNull
	Long sportId,

	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	String routineName
) {
}
