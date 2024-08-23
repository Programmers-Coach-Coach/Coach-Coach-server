package site.coach_coach.coach_coach_server.action.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.action.domain.Action;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public record ActionDto(
	@NotNull
	Long actionId,

	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	String actionName,

	Integer sets,

	String countOrMinutes,

	@Size(max = 200)
	String description
) {
	public static ActionDto from(Action action) {
		return new ActionDto(action.getActionId(), action.getActionName(), action.getSets(), action.getCountOrMinutes(),
			action.getDescription());
	}
}
