package site.coach_coach.coach_coach_server.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessage {
	public static final String ACCESS_DENIED = "접근 권한이 없습니다.";
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
	public static final String NOT_FOUND_CATEGORY = "존재하지 않는 카테고리입니다.";
	public static final String NOT_FOUND_ACTION = "존재하지 않는 운동입니다.";
	public static final String NOT_FOUND_RECORD = "존재하지 않는 기록입니다.";
	public static final String NOT_FOUND_NOTIFICATION = "해당 알림을 찾을 수 없습니다.";
	public static final String NOT_FOUND_COMPLETED_CATEGORY = "아직 완료되지 않은 카테고리입니다.";

	public static final String EXPIRED_TOKEN = "만료된 토큰입니다.";
	public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";

	public static final String CANNOT_CONTACT_SELF = "자기 자신에게는 문의할 수 없습니다.";
	public static final String CANNOT_MATCHING_SELF = "자기 자신을 매칭 회원으로 등록할 수 없습니다.";
	public static final String CANNOT_LIKE_SELF = "자기 자신을 관심 코치로 등록할 수 없습니다.";
	public static final String NOT_FOUND_MATCHING = "매칭되지 않은 대상입니다.";
	public static final String NOT_FOUND_CONTACT = "문의 회원이 아닙니다.";
	public static final String DUPLICATE_MATCHING = "이미 매칭된 회원입니다.";
	public static final String DUPLICATE_COMPLETED_CATEGORY = "이미 완료한 카테고리입니다.";

	public static final String NOT_FOUND_SPORTS = "종목 정보를 찾을 수 없습니다.";
	public static final String SERVER_SHUTDOWN = "서버가 종료되었습니다.";
	public static final String NOT_FOUND_PAGE = "페이지 정보를 찾을 수 없습니다.";
	public static final String DUPLICATE_CONTACT = "이미 해당 코치에 대한 문의 요청이 존재합니다.";
	public static final String DUPLICATE_RECORD = "입력하신 날짜에 대한 기록이 이미 존재합니다.";
	public static final String ALREADY_EXISTS_REVIEW = "이미 해당 코치에 대한 리뷰가 존재합니다.";
	public static final String CONVERT_FAIL = "파일 변환에 실패했습니다.";
	public static final String INVALID_FILE_EXTENSION = "허용된 파일 확장자가 아닙니다.";
	public static final String INVALID_FILE_NAME = "유효하지 않은 파일 이름입니다.";
	public static final String INVALID_FILE_SIZE = "파일 크기는 5MB 미만이어야 합니다.";
}
