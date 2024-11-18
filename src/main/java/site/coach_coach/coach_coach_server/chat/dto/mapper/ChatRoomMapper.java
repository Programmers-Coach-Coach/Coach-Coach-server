package site.coach_coach.coach_coach_server.chat.dto.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import site.coach_coach.coach_coach_server.chat.domain.ChatMessage;
import site.coach_coach.coach_coach_server.chat.domain.ChatRoom;
import site.coach_coach.coach_coach_server.chat.dto.response.CoachChatRoomsResponse;
import site.coach_coach.coach_coach_server.chat.dto.response.UserChatRoomsResponse;
import site.coach_coach.coach_coach_server.chat.repository.ChatMessageRepository;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.sport.domain.CoachingSport;
import site.coach_coach.coach_coach_server.user.domain.User;

public class ChatRoomMapper {
	public static UserChatRoomsResponse toUserChatRoomsResponse(
		ChatRoom chatRoom,
		Long userId,
		ChatMessageRepository chatMessageRepository
	) {
		Coach coach = chatRoom.getCoach();
		User coachUser = coach.getUser();

		long unreadCount = chatMessageRepository.countByChatRoomIdAndSenderIdNotAndIsReadFalse(
			chatRoom.getChatRoomId(),
			userId
		);
		Optional<ChatMessage> lastMessage = findLastMessage(chatRoom, chatMessageRepository);
		return new UserChatRoomsResponse(
			chatRoom.getChatRoomId(),
			coach.getCoachId(),
			coachUser.getNickname(),
			coachUser.getProfileImageUrl(),
			chatRoom.getMatching() != null && chatRoom.getMatching().getIsMatching(),
			getCoachingSports(coach),
			coach.getActiveHours(),
			lastMessage.map(ChatMessage::getMessage).orElse(""),
			unreadCount,
			lastMessage.map(ChatMessage::getCreatedAt).orElse(null)
		);
	}

	public static CoachChatRoomsResponse toCoachChatRoomsResponse(
		ChatRoom chatRoom,
		Long userId,
		ChatMessageRepository chatMessageRepository
	) {
		User user = chatRoom.getUser();
		Optional<ChatMessage> lastMessage = findLastMessage(chatRoom, chatMessageRepository);

		long unreadCount = chatMessageRepository.countByChatRoomIdAndSenderIdNotAndIsReadFalse(
			chatRoom.getChatRoomId(),
			userId
		);

		return new CoachChatRoomsResponse(
			chatRoom.getChatRoomId(),
			user.getUserId(),
			user.getNickname(),
			user.getProfileImageUrl(),
			chatRoom.getMatching() != null && chatRoom.getMatching().getIsMatching(),
			lastMessage.map(ChatMessage::getMessage).orElse(""),
			unreadCount,
			lastMessage.map(ChatMessage::getCreatedAt).orElse(null)
		);
	}

	private static Optional<ChatMessage> findLastMessage(
		ChatRoom chatRoom,
		ChatMessageRepository chatMessageRepository
	) {
		return chatMessageRepository
			.findTopByChatRoomIdOrderByCreatedAt(chatRoom.getChatRoomId());
	}

	private static List<String> getCoachingSports(Coach coach) {
		return coach.getCoachingSports().stream()
			.map(CoachingSport::getSportName)
			.collect(Collectors.toList());
	}
}
