package site.coach_coach.coach_coach_server.action.dto;

import jakarta.validation.constraints.NotBlank;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public record CreateActionRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	String actionName,

	int sets,

	String countOrMinutes,

	String description
) {
}
