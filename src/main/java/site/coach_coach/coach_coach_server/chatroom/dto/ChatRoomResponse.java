package site.coach_coach.coach_coach_server.chatroom.dto;

import java.time.LocalDateTime;

public record ChatRoomResponse(
	Long chatRoomId,
	String nickname,
	String profileImageUrl,
	String recentMessage,
	LocalDateTime updatedAt
) {
}
