package site.coach_coach.coach_coach_server.chat.dto.request;

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
