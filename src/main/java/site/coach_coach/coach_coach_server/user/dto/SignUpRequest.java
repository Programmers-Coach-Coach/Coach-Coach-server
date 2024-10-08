package site.coach_coach.coach_coach_server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.user.validation.Nickname;
import site.coach_coach.coach_coach_server.user.validation.Password;

public record SignUpRequest(
	@Nickname
	String nickname,

	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Email(message = ErrorMessage.INVALID_EMAIL)
	@Size(max = 45, message = ErrorMessage.INVALID_VALUE)
	String email,

	@Password
	String password
) {
}
