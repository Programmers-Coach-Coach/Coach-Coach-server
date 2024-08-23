package site.coach_coach.coach_coach_server.action.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.action.domain.Action;

public record ActionDto(
	@NotNull
	Long actionId,

	@NotNull
	String actionName,

	int sets,

	String countOrMinutes,

	@Size(max = 200)
	String description
) {
	public static ActionDto from(Action action) {
		return new ActionDto(action.getActionId(), action.getActionName(), action.getSets(), action.getCountOrMinutes(),
			action.getDescription());
	}
}
