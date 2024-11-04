package site.coach_coach.coach_coach_server.chat.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.chat.domain.ChatMessage;
import site.coach_coach.coach_coach_server.chat.domain.ChatRoom;
import site.coach_coach.coach_coach_server.chat.dto.ChatRoomRequest;
import site.coach_coach.coach_coach_server.chat.dto.ChatRoomResponse;
import site.coach_coach.coach_coach_server.chat.repository.ChatMessageRepository;
import site.coach_coach.coach_coach_server.chat.repository.ChatRoomRepository;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.coach.service.CoachService;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;
import site.coach_coach.coach_coach_server.common.exception.UserNotFoundException;
import site.coach_coach.coach_coach_server.matching.domain.Matching;
import site.coach_coach.coach_coach_server.matching.repository.MatchingRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final UserRepository userRepository;
	private final CoachRepository coachRepository;
	private final MatchingRepository matchingRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final CoachService coachService;
	private final ChatMessageRepository chatMessageRepository;

	public Long createChatRoom(ChatRoomRequest chatRoomRequest) {
		User user = userRepository.findById(chatRoomRequest.userId())
			.orElseThrow(UserNotFoundException::new);

		Coach coach = coachRepository.findById(chatRoomRequest.coachId())
			.orElseThrow(UserNotFoundException::new);

		Matching matching = matchingRepository.findById(chatRoomRequest.matchingId())
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_MATCHING));

		ChatRoom chatRoom = ChatRoom.builder()
			.coach(coach)
			.user(user)
			.matching(matching)
			.build();
		chatRoomRepository.save(chatRoom);
		return chatRoom.getChatRoomId();
	}

	public List<ChatRoomResponse> findChatRoomsForUser(Long userId) {
		return chatRoomRepository.findByUser_UserId(userId).stream()
			.map(this::toChatRoomResponseForUser)
			.collect(Collectors.toList());
	}

	public List<ChatRoomResponse> findChatRoomsForCoach(Long userId) {
		Coach coach = coachService.getCoachByUserId(userId);
		return chatRoomRepository.findByCoach_CoachId(coach.getCoachId()).stream()
			.map(this::toChatRoomResponseForCoach)
			.collect(Collectors.toList());
	}

	private ChatRoomResponse toChatRoomResponseForUser(ChatRoom chatRoom) {
		Coach coach = chatRoom.getCoach();
		User coachUser = coach.getUser();
		return buildChatRoomResponse(
			chatRoom, coachUser.getNickname(), coachUser.getProfileImageUrl()
		);
	}

	private ChatRoomResponse toChatRoomResponseForCoach(ChatRoom chatRoom) {
		User user = chatRoom.getUser();
		return buildChatRoomResponse(
			chatRoom, user.getNickname(), user.getProfileImageUrl()
		);
	}

	private ChatRoomResponse buildChatRoomResponse(ChatRoom chatRoom, String nickname, String profileImageUrl) {
		Optional<ChatMessage> lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAt(
			chatRoom.getChatRoomId()).stream().findFirst();
		return new ChatRoomResponse(
			chatRoom.getChatRoomId(),
			nickname,
			profileImageUrl,
			chatRoom.getMatching() != null && chatRoom.getMatching().getIsMatching(),
			lastMessage.map(ChatMessage::getMessage).orElse(""),
			lastMessage.map(ChatMessage::getCreatedAt).orElse(null)
		);
	}
}
