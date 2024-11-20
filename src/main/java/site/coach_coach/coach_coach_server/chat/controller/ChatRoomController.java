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
import site.coach_coach.coach_coach_server.chat.dto.response.ChatMessageResponse;
import site.coach_coach.coach_coach_server.chat.dto.response.CoachChatRoomsResponse;
import site.coach_coach.coach_coach_server.chat.dto.response.UserChatRoomsResponse;
import site.coach_coach.coach_coach_server.chat.service.ChatMessageService;
import site.coach_coach.coach_coach_server.chat.service.ChatRoomService;
import site.coach_coach.coach_coach_server.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {
	private final ChatRoomService chatRoomService;
	private final ChatMessageService chatMessageService;

	@GetMapping("/v1/users/chat-rooms")
	public ResponseEntity<List<UserChatRoomsResponse>> getUserChatRooms(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		User user = userDetails.getUser();
		List<UserChatRoomsResponse> chatRoomResponses = chatRoomService.findChatRoomsForUser(user);
		return ResponseEntity.ok(chatRoomResponses);
	}

	@GetMapping("/v1/coaches/chat-rooms")
	public ResponseEntity<List<CoachChatRoomsResponse>> getCoachChatRooms(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		User user = userDetails.getUser();
		List<CoachChatRoomsResponse> chatRoomResponses = chatRoomService.findChatRoomsForCoach(user);
		return ResponseEntity.ok(chatRoomResponses);
	}

	@GetMapping("/v1/chat-rooms/{chatRoomId}/messages")
	public ResponseEntity<Slice<ChatMessageResponse>> getChatMessages(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "chatRoomId") Long chatRoomId,
		Pageable pageable
	) {
		Long userId = userDetails.getUserId();
		chatMessageService.markMessagesAsRead(chatRoomId, userId);
		Slice<ChatMessageResponse> messages = chatRoomService
			.findChatMessagesByChatRoomId(userId, chatRoomId, pageable);
		return ResponseEntity.ok(messages);
	}
}
