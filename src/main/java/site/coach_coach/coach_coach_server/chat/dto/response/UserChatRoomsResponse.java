package site.coach_coach.coach_coach_server.chat.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record UserChatRoomsResponse(
	Long chatRoomId,
	Long coachId,
	String coachNickname,
	String coachProfileImageUrl,
	boolean isMatching,
	List<String> coachingSports,
	String activeHours,
	String lastMessage,
	LocalDateTime lastMessageCreatedAt
) {
}
