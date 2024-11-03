package site.coach_coach.coach_coach_server.chatroom.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.chatroom.dto.ChatRoomResponse;
import site.coach_coach.coach_coach_server.chatroom.service.ChatRoomService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {
	ChatRoomService chatRoomService;

	@GetMapping("/v1/users/chatrooms")
	public ResponseEntity<List<ChatRoomResponse>> getAllUserChatRooms(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		List<ChatRoomResponse> chatRoomResponses = chatRoomService.getAllUserChatRooms(userId);
		return ResponseEntity.ok(chatRoomResponses);
	}

	@GetMapping("/v1/coaches/chatrooms")
	public ResponseEntity<List<ChatRoomResponse>> getAllCoachChatRooms(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		List<ChatRoomResponse> chatRoomResponses = chatRoomService.getAllCoachChatRooms(userId);
		return ResponseEntity.ok(chatRoomResponses);
	}
}
