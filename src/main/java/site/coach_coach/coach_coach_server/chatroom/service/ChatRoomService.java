package site.coach_coach.coach_coach_server.chatroom.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.chatroom.domain.ChatRoom;
import site.coach_coach.coach_coach_server.chatroom.dto.ChatRoomRequest;
import site.coach_coach.coach_coach_server.chatroom.dto.ChatRoomResponse;
import site.coach_coach.coach_coach_server.chatroom.repository.ChatRoomRepository;
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

	public Long createChatRoom(ChatRoomRequest chatRoomRequest) {
		User user = userRepository.findById(chatRoomRequest.userId())
			.orElseThrow(UserNotFoundException::new);

		Coach coach = coachRepository.findById(chatRoomRequest.coachId())
			.orElseThrow(UserNotFoundException::new);

		Matching matching = matchingRepository.findById(chatRoomRequest.matchingId())
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_MATCHING));

		ChatRoom chatRoom = toChatRoomEntity(coach, user, matching);
		chatRoomRepository.save(chatRoom);

		return chatRoom.getChatRoomId();
	}

	public List<ChatRoomResponse> getAllUserChatRooms(Long userId) {
		List<ChatRoom> chatRooms = chatRoomRepository.findByUser_UserId(userId);
		return chatRooms.stream()
			.map(chatRoom -> {
				Coach coach = chatRoom.getCoach();
				User coachUser = coach.getUser();
				boolean isMatching = chatRoom.getMatching() != null && chatRoom.getMatching().getIsMatching();

				return new ChatRoomResponse(
					chatRoom.getChatRoomId(),
					coachUser.getNickname(),
					coachUser.getProfileImageUrl(),
					isMatching
				);
			})
			.collect(Collectors.toList());
	}

	public List<ChatRoomResponse> getAllCoachChatRooms(Long userId) {
		Coach coach = coachService.getCoachByUserId(userId);
		List<ChatRoom> chatRooms = chatRoomRepository.findByCoach_CoachId(coach.getCoachId());

		return chatRooms.stream()
			.map(chatRoom -> {
				User user = chatRoom.getUser();
				boolean isMatching = chatRoom.getMatching() != null && chatRoom.getMatching().getIsMatching();

				return new ChatRoomResponse(
					chatRoom.getChatRoomId(),
					user.getNickname(),
					user.getProfileImageUrl(),
					isMatching
				);
			})
			.collect(Collectors.toList());
	}

	private ChatRoom toChatRoomEntity(Coach coach, User user, Matching matching) {
		return ChatRoom.builder()
			.coach(coach)
			.user(user)
			.matching(matching)
			.build();
	}
}
