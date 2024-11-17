package site.coach_coach.coach_coach_server.chat.service;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.chat.domain.ChatMessage;
import site.coach_coach.coach_coach_server.chat.domain.ChatRoom;
import site.coach_coach.coach_coach_server.chat.dto.mapper.ChatMessageMapper;
import site.coach_coach.coach_coach_server.chat.dto.request.ChatMessageRequest;
import site.coach_coach.coach_coach_server.chat.repository.ChatMessageRepository;
import site.coach_coach.coach_coach_server.chat.repository.ChatRoomRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.domain.RoleEnum;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final SimpMessageSendingOperations messagingTemplate;

	@Transactional
	public void addMessage(Long chatRoomId, ChatMessageRequest messageRequest) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CHAT_ROOM));
		ChatMessage chatMessage = chatMessageRepository.save(
			ChatMessageMapper.toChatMessage(messageRequest, chatRoomId)
		);
		messagingTemplate.convertAndSend("/sub/chat-rooms/" + chatRoomId,
			ChatMessageMapper.toChatMessageResponse(chatMessage));
	}

	private RoleEnum determineSenderRole(ChatRoom chatRoom, Long senderId) {
		if (chatRoom.getUser().getUserId().equals(senderId)) {
			return RoleEnum.USER;
		} else if (chatRoom.getCoach().getUser().getUserId().equals(senderId)) {
			return RoleEnum.COACH;
		} else {
			throw new AccessDeniedException();
		}
	}
}
