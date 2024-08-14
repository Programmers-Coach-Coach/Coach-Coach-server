package site.coach_coach.coach_coach_server.routine.exception;

import java.util.NoSuchElementException;

public class NotMatchingException extends NoSuchElementException {
	public NotMatchingException(String message) {
		super(message);
	}
}
