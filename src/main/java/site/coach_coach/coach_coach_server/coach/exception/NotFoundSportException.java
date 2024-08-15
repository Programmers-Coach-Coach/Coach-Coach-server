package site.coach_coach.coach_coach_server.coach.exception;

import java.util.NoSuchElementException;

public class NotFoundSportException extends NoSuchElementException {
	public NotFoundSportException(String message) {
		super(message);
	}
}
