package site.coach_coach.coach_coach_server.category.exception;

import java.util.NoSuchElementException;

public class NotFoundCategoryException extends NoSuchElementException {
	public NotFoundCategoryException(String message) {
		super(message);
	}
}
