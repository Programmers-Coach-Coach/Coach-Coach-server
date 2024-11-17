package site.coach_coach.coach_coach_server.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(
	@NotBlank
	String message
) {
}
