package site.coach_coach.coach_coach_server.action.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public record CreateActionRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	String actionName,

	Integer sets,
	Integer counts,
	Integer minutes,

	@Size(max = 200)
	String description
) {
}
