package site.coach_coach.coach_coach_server.coach.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateReviewException extends RuntimeException {
	public DuplicateReviewException(String message) {
		super(message);
	}
}
