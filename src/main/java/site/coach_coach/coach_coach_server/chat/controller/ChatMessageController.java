package site.coach_coach.coach_coach_server.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.chat.dto.request.ChatMessageRequest;
import site.coach_coach.coach_coach_server.chat.dto.response.ChatMessageResponse;
import site.coach_coach.coach_coach_server.chat.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

	@MessageMapping("/chat-rooms/{chatRoomId}")
	@SendTo("/sub/chat-rooms/{chatRoomId}")
	public ChatMessageResponse sendMessage(
		@DestinationVariable("chatRoomId") Long chatRoomId,
		@Payload ChatMessageRequest messageRequest
	) {
		return chatMessageService.addMessage(chatRoomId, messageRequest);
	}
}
