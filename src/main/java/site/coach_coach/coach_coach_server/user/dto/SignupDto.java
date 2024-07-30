package site.coach_coach.coach_coach_server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.validation.Nickname;
import site.coach_coach.coach_coach_server.user.validation.Password;

public record SignupDto(
	@NotNull(message = ErrorMessage.INVALID_VALUE)
	Boolean isCoach,

	@Nickname
	String nickname,

	@NotBlank(message = ErrorMessage.EMPTY_EMAIL)
	@Email(message = ErrorMessage.INVALID_EMAIL)
	String email,

	@Password
	String password
) {
}
