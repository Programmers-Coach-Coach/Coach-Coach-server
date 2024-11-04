package site.coach_coach.coach_coach_server.chat.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	private String message;
	private LocalDateTime createdAt;
}
