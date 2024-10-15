package site.coach_coach.coach_coach_server.user.dto;

import site.coach_coach.coach_coach_server.user.validation.Nickname;

public record NicknameRequest(
	@Nickname
	String nickname
) {
}
