package site.coach_coach.coach_coach_server.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.chat.domain.ChatRoom;
import site.coach_coach.coach_coach_server.chat.dto.mapper.ChatMessageMapper;
import site.coach_coach.coach_coach_server.chat.dto.mapper.ChatRoomMapper;
import site.coach_coach.coach_coach_server.chat.dto.request.ChatRoomRequest;
import site.coach_coach.coach_coach_server.chat.dto.response.ChatMessageResponse;
import site.coach_coach.coach_coach_server.chat.dto.response.CoachChatRoomsResponse;
import site.coach_coach.coach_coach_server.chat.dto.response.UserChatRoomsResponse;
import site.coach_coach.coach_coach_server.chat.repository.ChatMessageRepository;
import site.coach_coach.coach_coach_server.chat.repository.ChatRoomRepository;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.coach.service.CoachService;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
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

	@Transactional
	public Long createChatRoom(ChatRoomRequest chatRoomRequest) {
		User user = userRepository.findById(chatRoomRequest.userId())
			.orElseThrow(UserNotFoundException::new);

		Coach coach = coachRepository.findById(chatRoomRequest.coachId())
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));

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
			.map(chatRoom -> ChatRoomMapper.toUserChatRoomsResponse(chatRoom, chatMessageRepository))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<CoachChatRoomsResponse> findChatRoomsForCoach(Long userId) {
		Coach coach = coachService.getCoachByUserId(userId);
		return chatRoomRepository.findByCoach_CoachId(coach.getCoachId())
			.stream()
			.map(chatRoom -> ChatRoomMapper.toCoachChatRoomsResponse(chatRoom, chatMessageRepository))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Slice<ChatMessageResponse> findChatMessagesByChatRoomId(Long userId, Long chatRoomId, Pageable pageable) {
		validateUserRoleForChatRoom(userId, chatRoomId);
		return chatMessageRepository
			.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable)
			.map(ChatMessageMapper::toChatMessageResponse);
	}

	private void validateUserRoleForChatRoom(Long userId, Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CHAT_ROOM));

		boolean isUser = chatRoom.getUser().getUserId().equals(userId);
		boolean isCoach = chatRoom.getCoach() != null
			&& chatRoom.getCoach().getUser().getUserId().equals(userId);

		if (!isUser && !isCoach) {
			throw new AccessDeniedException();
		}
	}
}
