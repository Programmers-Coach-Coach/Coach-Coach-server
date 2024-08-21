package site.coach_coach.coach_coach_server.coach.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateContactException extends RuntimeException {
	public DuplicateContactException(String message) {
		super(message);
	}
}
