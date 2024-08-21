package site.coach_coach.coach_coach_server.common.exception;

import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public class AccessDeniedException extends RuntimeException {
	public AccessDeniedException() {
		super(ErrorMessage.ACCESS_DENIED);
	}
}
