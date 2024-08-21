package site.coach_coach.coach_coach_server.notification.exception;

import java.util.NoSuchElementException;

public class NotFoundException extends NoSuchElementException {
	public NotFoundException(String message) {
		super(message);
	}
}
