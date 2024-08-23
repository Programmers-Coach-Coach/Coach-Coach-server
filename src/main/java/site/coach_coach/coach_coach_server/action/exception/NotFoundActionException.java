package site.coach_coach.coach_coach_server.action.exception;

import java.util.NoSuchElementException;

public class NotFoundActionException extends NoSuchElementException {
	public NotFoundActionException(String message) {
		super(message);
	}
}
