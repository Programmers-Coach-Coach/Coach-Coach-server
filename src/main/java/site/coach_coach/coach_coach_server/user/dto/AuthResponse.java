package site.coach_coach.coach_coach_server.user.dto;

import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;

@Builder
public record AuthResponse(
	boolean isLogin,
	String nickname,
	GenderEnum gender,
	String profileImageUrl,
	boolean isCoach,
	Integer countOfNotifications
) {
}
