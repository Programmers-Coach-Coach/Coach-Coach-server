package site.coach_coach.coach_coach_server.user.dto;

import site.coach_coach.coach_coach_server.user.validation.Password;

public record PasswordRequest(
	@Password
	String password
) {
}
