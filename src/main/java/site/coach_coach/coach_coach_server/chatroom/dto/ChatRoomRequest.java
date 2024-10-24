package site.coach_coach.coach_coach_server.chatroom.dto;

import jakarta.validation.constraints.NotNull;

public record ChatRoomRequest(
	@NotNull
	Long userId,

	@NotNull
	Long coachId,

	@NotNull
	Long matchingId
) {
}
