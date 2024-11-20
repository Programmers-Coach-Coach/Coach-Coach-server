package site.coach_coach.coach_coach_server.chat.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import site.coach_coach.coach_coach_server.chat.domain.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAt(Long chatRoomId);

	Slice<ChatMessage> findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);

	long countByChatRoomIdAndSenderIdNotAndIsReadFalse(Long chatRoomId, Long senderId);

	@Modifying
	@Query("{'chatRoomId': ?0, 'senderId': {$ne: ?1}, 'isRead': false}")
	void markMessagesAsRead(Long chatRoomId, Long senderId);
}
