package site.coach_coach.coach_coach_server.chatroom.dto;

public record ChatRoomResponse(
	Long chatRoomId,
	String nickname,
	String profileImageUrl,
	boolean isMatching
) {
}
