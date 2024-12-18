package site.coach_coach.coach_coach_server.chat.dto.mapper;

import site.coach_coach.coach_coach_server.chat.domain.ChatMessage;
import site.coach_coach.coach_coach_server.chat.dto.response.ChatMessageResponse;

public class ChatMessageMapper {
	public static ChatMessageResponse toChatMessageResponse(
		ChatMessage chatMessage
	) {
		return new ChatMessageResponse(
			chatMessage.getSenderId(),
			chatMessage.getSenderRole(),
			chatMessage.getMessage(),
			chatMessage.isRead(),
			chatMessage.getCreatedAt()
		);
	}
}
