package site.coach_coach.coach_coach_server.common.exception;

import java.util.NoSuchElementException;

public class NotFoundException extends NoSuchElementException {
	public NotFoundException(String message) {
		super(message);
	}
}
