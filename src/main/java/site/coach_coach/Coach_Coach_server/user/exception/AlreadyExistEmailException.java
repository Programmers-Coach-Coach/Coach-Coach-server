package site.coach_coach.Coach_Coach_server.user.exception;

import org.springframework.dao.DuplicateKeyException;

public class AlreadyExistEmailException extends DuplicateKeyException {
	public static final String ERROR_MESSAGE = "이미 존재하는 이메일입니다.";

	public AlreadyExistEmailException() {
		super(ERROR_MESSAGE);
	}
}
