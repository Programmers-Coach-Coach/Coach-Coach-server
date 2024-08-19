package site.coach_coach.coach_coach_server.action.dto;

import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.action.domain.Action;

public record ActionDto(
	@NotNull
	Long actionId,

	@NotNull
	String actionName,

	String sets,

	String countOrMinutes,

	String description
) {
	public static ActionDto from(Action action) {
		return new ActionDto(action.getActionId(), action.getActionName(), action.getSets(), action.getCountOrMinutes(),
			action.getDescription());
	}
}
