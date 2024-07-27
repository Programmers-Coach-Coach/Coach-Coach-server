package site.coach_coach.Coach_Coach_server.user.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessage {
	public static final String INVALID_NICKNAME = "닉네임 형식이 올바르지 않습니다.";
	public static final String INVALID_EMAIL = "이메일 형식이 올바르지 않습니다.";
	public static final String INVALID_PASSWORD = "비밀번호 형식이 올바르지 않습니다.";

	public static final String EMPTY_NICKNAME = "닉네임을 입력해주세요.";
	public static final String EMPTY_EMAIL = "이메일을 입력해주세요.";
	public static final String EMPTY_PASSWORD = "비밀번호를 입력해주세요.";
}
