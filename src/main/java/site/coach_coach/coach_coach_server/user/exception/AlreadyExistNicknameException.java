package site.coach_coach.coach_coach_server.user.exception;

import org.springframework.dao.DuplicateKeyException;

public class AlreadyExistNicknameException extends DuplicateKeyException {
	public static final String ERROR_MESSAGE = "이미 사용 중인 닉네임입니다.";

	public AlreadyExistNicknameException() {
		super(ERROR_MESSAGE);
	}
}
