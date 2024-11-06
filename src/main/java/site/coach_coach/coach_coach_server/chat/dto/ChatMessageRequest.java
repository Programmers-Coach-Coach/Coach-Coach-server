package site.coach_coach.coach_coach_server.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.common.domain.RoleEnum;

public record ChatMessageRequest(
	@NotNull
	Long chatRoomId,

	@NotNull
	Long senderId,

	@NotBlank
	RoleEnum senderRole,

	@NotBlank
	String message
) {
}
