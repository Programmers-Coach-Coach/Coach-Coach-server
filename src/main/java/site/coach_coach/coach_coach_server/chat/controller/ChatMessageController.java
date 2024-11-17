package site.coach_coach.coach_coach_server.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.chat.dto.request.ChatMessageRequest;
import site.coach_coach.coach_coach_server.chat.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

	@MessageMapping("/chat-rooms/{chatRoomId}")
	public void sendMessage(
		@DestinationVariable("chatRoomId") Long chatRoomId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Payload ChatMessageRequest messageRequest
	) {
		Long senderId = userDetails.getUserId();
		chatMessageService.addMessage(chatRoomId, senderId, messageRequest);
	}
}
