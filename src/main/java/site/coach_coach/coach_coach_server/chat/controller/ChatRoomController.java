package site.coach_coach.coach_coach_server.chat.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.chat.dto.ChatMessageResponse;
import site.coach_coach.coach_coach_server.chat.dto.CoachChatRoomsResponse;
import site.coach_coach.coach_coach_server.chat.dto.UserChatRoomsResponse;
import site.coach_coach.coach_coach_server.chat.service.ChatRoomService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {
	ChatRoomService chatRoomService;

	@GetMapping("/v1/users/chat-rooms")
	public ResponseEntity<List<UserChatRoomsResponse>> getUserChatRooms(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		List<UserChatRoomsResponse> chatRoomResponses = chatRoomService.findChatRoomsForUser(userId);
		return ResponseEntity.ok(chatRoomResponses);
	}

	@GetMapping("/v1/coaches/chat-rooms")
	public ResponseEntity<List<CoachChatRoomsResponse>> getCoachChatRooms(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		List<CoachChatRoomsResponse> chatRoomResponses = chatRoomService.findChatRoomsForCoach(userId);
		return ResponseEntity.ok(chatRoomResponses);
	}

	@GetMapping("/v1/chat-rooms/{chatRoomId}/messages")
	public ResponseEntity<Slice<ChatMessageResponse>> getChatMessages(
		@PathVariable(name = "chatRoomId") Long chatRoomId,
		Pageable pageable
	) {
		Slice<ChatMessageResponse> messages = chatRoomService.findChatMessagesByChatRoomId(chatRoomId, pageable);
		return ResponseEntity.ok(messages);
	}
}
