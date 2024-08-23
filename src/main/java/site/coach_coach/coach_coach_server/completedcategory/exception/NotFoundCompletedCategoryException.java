package site.coach_coach.coach_coach_server.completedcategory.exception;

import java.util.NoSuchElementException;

public class NotFoundCompletedCategoryException extends NoSuchElementException {
	public NotFoundCompletedCategoryException(String message) {
		super(message);
	}
}
