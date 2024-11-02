package site.coach_coach.coach_coach_server.action.dto;

import lombok.Builder;
import site.coach_coach.coach_coach_server.action.domain.Action;

@Builder
public record ActionDto(
	Long actionId,
	String actionName,
	Integer sets,
	Integer countsOrMinutes
) {
	public static ActionDto from(Action action) {
		return new ActionDto(
			action.getActionId(),
			action.getActionName(),
			action.getSets(),
			action.getCountsOrMinutes()
		);
	}
}
