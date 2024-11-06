package site.coach_coach.coach_coach_server.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/message")
	public void sendMessage(@Payload final String message) {
	}
}
