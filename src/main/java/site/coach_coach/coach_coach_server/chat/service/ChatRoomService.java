package site.coach_coach.coach_coach_server.chat.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.chat.domain.ChatMessage;
import site.coach_coach.coach_coach_server.chat.domain.ChatRoom;
import site.coach_coach.coach_coach_server.chat.dto.ChatMessageResponse;
import site.coach_coach.coach_coach_server.chat.dto.ChatRoomRequest;
import site.coach_coach.coach_coach_server.chat.dto.CoachChatRoomsResponse;
import site.coach_coach.coach_coach_server.chat.dto.UserChatRoomsResponse;
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
import site.coach_coach.coach_coach_server.sport.domain.CoachingSport;
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

	@Transactional
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

	@Transactional(readOnly = true)
	public List<UserChatRoomsResponse> findChatRoomsForUser(Long userId) {
		return chatRoomRepository.findByUser_UserId(userId)
			.stream()
			.map(this::toChatRoomResponseForUser)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<CoachChatRoomsResponse> findChatRoomsForCoach(Long userId) {
		Coach coach = coachService.getCoachByUserId(userId);
		return chatRoomRepository.findByCoach_CoachId(coach.getCoachId())
			.stream()
			.map(this::toChatRoomResponseForCoach)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Slice<ChatMessageResponse> getChatMessages(Long chatRoomId, Pageable pageable) {
		return chatMessageRepository
			.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable)
			.map(this::toChatMessageResponse);
	}

	private UserChatRoomsResponse toChatRoomResponseForUser(ChatRoom chatRoom) {
		Coach coach = chatRoom.getCoach();
		User coachUser = coach.getUser();
		return new UserChatRoomsResponse(
			chatRoom.getChatRoomId(),
			coach.getCoachId(),
			coachUser.getNickname(),
			coachUser.getProfileImageUrl(),
			chatRoom.getMatching() != null && chatRoom.getMatching().getIsMatching(),
			getCoachingSports(coach),
			coach.getActiveHours(),
			getLastMessage(chatRoom).map(ChatMessage::getMessage).orElse(""),
			getLastMessage(chatRoom).map(ChatMessage::getCreatedAt).orElse(null)
		);
	}

	private CoachChatRoomsResponse toChatRoomResponseForCoach(ChatRoom chatRoom) {
		User user = chatRoom.getUser();
		return new CoachChatRoomsResponse(
			chatRoom.getChatRoomId(),
			user.getUserId(),
			user.getNickname(),
			user.getProfileImageUrl(),
			chatRoom.getMatching() != null && chatRoom.getMatching().getIsMatching(),
			getLastMessage(chatRoom).map(ChatMessage::getMessage).orElse(""),
			getLastMessage(chatRoom).map(ChatMessage::getCreatedAt).orElse(null)
		);
	}

	private Optional<ChatMessage> getLastMessage(ChatRoom chatRoom) {
		return chatMessageRepository
			.findTopByChatRoomIdOrderByCreatedAt(chatRoom.getChatRoomId())
			.stream()
			.findFirst();
	}

	private List<String> getCoachingSports(Coach coach) {
		return coach.getCoachingSports().stream()
			.map(CoachingSport::getSportName)
			.collect(Collectors.toList());
	}

	private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
		return new ChatMessageResponse(
			chatMessage.getSenderId(),
			chatMessage.getSenderRole(),
			chatMessage.getMessage(),
			chatMessage.isRead(),
			chatMessage.getCreatedAt()
		);
	}
}
