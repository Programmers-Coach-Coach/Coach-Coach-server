package site.coach_coach.coach_coach_server.notification.dto;

import java.time.LocalDateTime;

import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;

public record NotificationResponse(
	Long notificationId,
	String message,
	RelationFunctionEnum relationFunction,
	LocalDateTime createdAt
) {
}
