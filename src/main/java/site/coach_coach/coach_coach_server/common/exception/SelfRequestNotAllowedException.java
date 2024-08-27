package site.coach_coach.coach_coach_server.common.exception;

public class SelfRequestNotAllowedException extends RuntimeException {
	public SelfRequestNotAllowedException(String message) {
		super(message);
	}
}
