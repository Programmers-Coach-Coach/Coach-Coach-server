package site.coach_coach.coach_coach_server.user.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
	Boolean isLogin,
	String nickname
) {
}
