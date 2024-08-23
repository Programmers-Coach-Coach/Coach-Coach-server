package site.coach_coach.coach_coach_server.completedcategory.exception;

import site.coach_coach.coach_coach_server.userrecord.exception.DuplicateRecordException;

public class DuplicateCompletedCategoryException extends DuplicateRecordException {
	public DuplicateCompletedCategoryException(String message) {
		super(message);
	}
}
