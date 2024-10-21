package site.coach_coach.coach_coach_server.action.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public record UpdateActionInfoRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Size(max = 45)
	String actionName,

	Integer sets,
	Integer countsOrMinutes
) {
}
