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
	UPDATE_PROFILE_SUCCESS("수정 완료"),
	CREATE_ROUTINE_SUCCESS("루틴 추가 성공"),
	DELETE_ROUTINE_SUCCESS("루틴 삭제 성공"),
	DELETE_CATEGORY_SUCCESS("카테고리 삭제 성공"),
	UPDATE_COACH_PROFILE_SUCCESS("업로드 성공"),
	DELETE_NOTIFICATION_SUCCESS("알림 삭제 성공"),
	MATCH_MEMBER_SUCCESS("관리 회원에 등록되었습니다."),
	CREATE_CONTACT_SUCCESS("문의 회원으로 등록되었습니다."),
  CREATE_LIKE_SUCCESS("관심 코치에 등록되었습니다."),
	DELETE_LIKE_SUCCESS("관심 코치 취소 되었습니다.");

	private final String message;

	SuccessMessage(String message) {
		this.message = message;
	}
}
