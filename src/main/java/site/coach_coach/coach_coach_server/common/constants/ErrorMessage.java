package site.coach_coach.coach_coach_server.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessage {
	public static final String INVALID_REQUEST = "유효하지 않은 요청입니다.";
	public static final String SERVER_ERROR = "서버 오류가 발생했습니다.";

	public static final String INVALID_VALUE = "형식에 올바르게 작성해주세요.";

	public static final String EMPTY_NICKNAME = "닉네임을 입력해주세요.";
	public static final String INVALID_NICKNAME = "닉네임 형식이 올바르지 않습니다.";

	public static final String EMPTY_EMAIL = "이메일을 입력해주세요.";
	public static final String INVALID_EMAIL = "이메일 형식이 올바르지 않습니다.";

	public static final String EMPTY_PASSWORD = "비밀번호를 입력해주세요.";
	public static final String INVALID_PASSWORD = "비밀번호 형식이 올바르지 않습니다.";
	public static final String INCORRECT_PASSWORD = "비밀번호가 일치하지 않습니다.";

	public static final String DUPLICATE_EMAIL = "이미 사용중인 이메일입니다.";
	public static final String DUPLICATE_NICKNAME = "이미 사용중인 닉네임입니다.";

	public static final String INVALID_USER = "회원 정보가 잘못되었습니다.";

	public static final String NOT_FOUND_USER = "존재하지 않는 회원입니다.";
	public static final String NOT_FOUND_COACH = "존재하지 않는 코치입니다.";
	public static final String NOT_FOUND_TOKEN = "토큰이 존재하지 않습니다.";
	public static final String NOT_FOUND_ROUTINE = "존재하지 않는 루틴입니다.";

	public static final String EXPIRED_TOKEN = "만료된 토큰입니다.";
	public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
	public static final String INVALID_ID = "유효하지 않은 ID 입니다.";

	public static final String NOT_MATCHING = "매칭되지 않은 대상입니다.";

	public static final String NOT_FOUND_SPORTS = "종목 정보가 없습니다.";
	public static final String SERVER_SHUTDOWN = "서버가 종료되었습니다.";
}

