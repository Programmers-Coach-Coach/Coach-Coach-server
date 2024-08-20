package site.coach_coach.coach_coach_server.notification.dto;

import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;
import site.coach_coach.coach_coach_server.common.validation.Enum;

public record NotificationRequest(
	@NotNull
	Long coachId,

	@NotNull
	@Enum(enumClass = RelationFunctionEnum.class)
	RelationFunctionEnum relationFunction
) {
}
