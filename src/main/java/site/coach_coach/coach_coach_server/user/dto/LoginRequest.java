package site.coach_coach.coach_coach_server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.user.validation.Password;

public record LoginRequest(
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	@Size(max = 45, message = "형식에 올바르게 작성해주세요.")
	String email,

	@Password
	String password
) {
}
