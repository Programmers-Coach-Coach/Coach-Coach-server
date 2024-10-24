package site.coach_coach.coach_coach_server.chatroom.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.chatroom.domain.ChatRoom;

public record ChatRoomListResponse(
	List<ChatRoom> chatRooms
) {
}
