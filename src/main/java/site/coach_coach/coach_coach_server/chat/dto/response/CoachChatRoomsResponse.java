package site.coach_coach.coach_coach_server.chat.dto.response;

import java.time.LocalDateTime;

public record CoachChatRoomsResponse(
	Long chatRoomId,
	Long userId,
	String userNickname,
	String userProfileImageUrl,
	boolean isMatching,
	String lastMessage,
	long unreadCount,
	LocalDateTime lastMessageCreatedAt
) {
}
