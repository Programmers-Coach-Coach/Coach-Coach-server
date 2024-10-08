package site.coach_coach.coach_coach_server.notification.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;
import site.coach_coach.coach_coach_server.notification.domain.Notification;

@Builder
public record NotificationListResponse(
	Long notificationId,
	String message,
	RelationFunctionEnum relationFunction,
	LocalDateTime createdAt
) {
	public static NotificationListResponse from(Notification notification) {
		return new NotificationListResponse(
			notification.getNotificationId(),
			notification.getMessage(),
			notification.getRelationFunction(),
			notification.getCreatedAt()
		);
	}
}
