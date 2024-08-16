package site.coach_coach.coach_coach_server.common.constants;

import lombok.Getter;

@Getter
public enum SuccessMessage {
	SIGNUP_SUCCESS("회원가입 성공"),
	LOGIN_SUCCESS("로그인 성공"),
	LOGOUT_SUCCESS("로그아웃 성공"),
	EMAIL_AVAILABLE("사용 가능한 이메일입니다."),
	NICKNAME_AVAILABLE("사용 가능한 닉네임입니다."),
	PASSWORD_CONFIRM_SUCCESS("비밀번호 확인 성공"),
	CREATE_ROUTINE_SUCCESS("루틴 추가 성공");

	private final String message;

	SuccessMessage(String message) {
		this.message = message;
	}
}
