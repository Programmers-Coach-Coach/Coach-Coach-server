package site.coach_coach.coach_coach_server.notification.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;
import site.coach_coach.coach_coach_server.notification.domain.Notification;

@Builder
public record NotificationResponse(
	Long notificationId,
	String message,
	RelationFunctionEnum relationFunction,
	LocalDateTime createdAt
) {
	public static NotificationResponse from(Notification notification) {
		return NotificationResponse.builder()
			.notificationId(notification.getNotificationId())
			.message(notification.getMessage())
			.relationFunction(notification.getRelationFunction())
			.createdAt(notification.getCreatedAt())
			.build();
	}
}
