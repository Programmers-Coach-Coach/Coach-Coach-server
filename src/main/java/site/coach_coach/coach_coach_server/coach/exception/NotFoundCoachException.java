package site.coach_coach.coach_coach_server.coach.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundCoachException extends NoSuchElementException {
	public NotFoundCoachException(String message) {
		super(message);
	}
}
