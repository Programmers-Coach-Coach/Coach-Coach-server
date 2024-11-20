package site.coach_coach.coach_coach_server.chat.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.RoleEnum;

@Document(collection = "chat_messages")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
	@Id
	private String id;
	private Long chatRoomId;
	private Long senderId;
	private RoleEnum senderRole;
	private String message;
	private boolean isRead;
	@CreationTimestamp
	private LocalDateTime createdAt;

	private ChatMessage(
		Long chatRoomId,
		Long senderId,
		RoleEnum senderRole,
		String message
	) {
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.senderRole = senderRole;
		this.message = message;
		this.isRead = false;
		this.createdAt = LocalDateTime.now();
	}

	public static ChatMessage of(
		Long chatRoomId,
		Long senderId,
		RoleEnum senderRole,
		String message
	) {
		return new ChatMessage(chatRoomId, senderId, senderRole, message);
	}
}
