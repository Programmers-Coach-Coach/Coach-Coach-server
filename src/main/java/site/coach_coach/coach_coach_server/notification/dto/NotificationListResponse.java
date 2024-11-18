package site.coach_coach.coach_coach_server.notification.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;

@Builder
public record NotificationListResponse(
	Long notificationId,
	String nickname,
	String profileImageUrl,
	String message,
	RelationFunctionEnum relationFunction,
	boolean isRead,
	LocalDateTime createdAt
) {
}
