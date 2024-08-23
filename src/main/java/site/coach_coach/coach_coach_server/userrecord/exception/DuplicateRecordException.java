package site.coach_coach.coach_coach_server.userrecord.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateRecordException extends DuplicateKeyException {
	public DuplicateRecordException(String message) {
		super(message);
	}
}
