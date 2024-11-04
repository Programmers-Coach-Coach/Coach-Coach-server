package site.coach_coach.coach_coach_server.chat.dto;

import java.time.LocalDateTime;

public record ChatRoomResponse(
	Long chatRoomId,
	String nickname,
	String profileImageUrl,
	boolean isMatching,
	String lastMessage,
	LocalDateTime createdAt
) {
}
