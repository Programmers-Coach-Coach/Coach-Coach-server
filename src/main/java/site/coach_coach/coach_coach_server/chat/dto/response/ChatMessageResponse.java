package site.coach_coach.coach_coach_server.chat.dto.response;

import java.time.LocalDateTime;

import site.coach_coach.coach_coach_server.common.domain.RoleEnum;

public record ChatMessageResponse(
	Long senderId,
	RoleEnum senderRole,
	String message,
	boolean isRead,
	LocalDateTime createdAt
) {
}
