package site.coach_coach.coach_coach_server.common.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
	INVALID_REQUEST("유효하지 않은 요청입니다."),
	SERVER_ERROR("서버 오류가 발생했습니다."),

	INVALID_VALUE("형식에 올바르게 작성해주세요."),

	EMPTY_NICKNAME("닉네임을 입력해주세요."),
	INVALID_NICKNAME("닉네임 형식이 올바르지 않습니다."),

	EMPTY_EMAIL("이메일을 입력해주세요."),
	INVALID_EMAIL("이메일 형식이 올바르지 않습니다."),

	EMPTY_PASSWORD("비밀번호를 입력해주세요."),
	INVALID_PASSWORD("비밀번호 형식이 올바르지 않습니다."),

	DUPLICATE_EMAIL("이미 사용중인 이메일입니다."),
	DUPLICATE_NICKNAME("이미 사용중인 닉네임입니다."),

	NOT_FOUND_USER("회원 정보가 없습니다."),

	NOT_FOUND_TOKEN("토큰이 존재하지 않습니다."),
	EXPIRED_TOKEN("만료된 토큰입니다."),
	INVALID_TOKEN("유효하지 않은 토큰입니다.");

	private final String message;
}
