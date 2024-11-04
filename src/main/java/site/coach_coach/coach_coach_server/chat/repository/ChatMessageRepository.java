package site.coach_coach.coach_coach_server.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import site.coach_coach.coach_coach_server.chat.domain.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	List<ChatMessage> findTopByChatRoomIdOrderByCreatedAt(Long chatRoomId);
}
