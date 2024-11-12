package site.coach_coach.coach_coach_server.chat.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

import site.coach_coach.coach_coach_server.chat.domain.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAt(Long chatRoomId);

	Slice<ChatMessage> findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);
}
