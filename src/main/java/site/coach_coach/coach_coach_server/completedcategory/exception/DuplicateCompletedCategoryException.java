package site.coach_coach.coach_coach_server.completedcategory.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateCompletedCategoryException extends DuplicateKeyException {
	public DuplicateCompletedCategoryException(String message) {
		super(message);
	}
}
