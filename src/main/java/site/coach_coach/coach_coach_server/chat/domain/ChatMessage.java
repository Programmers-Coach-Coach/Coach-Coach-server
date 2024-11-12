package site.coach_coach.coach_coach_server.chat.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.RoleEnum;

@Document(collection = "chat_messages")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
	@Id
	private String id;
	private Long chatRoomId;
	private Long senderId;
	private RoleEnum senderRole;
	private String message;
	private boolean isRead = false;
	@CreationTimestamp
	private LocalDateTime createdAt;
}
