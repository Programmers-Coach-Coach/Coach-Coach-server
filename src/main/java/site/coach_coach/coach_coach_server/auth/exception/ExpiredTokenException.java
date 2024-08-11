package site.coach_coach.coach_coach_server.auth.exception;

public class ExpiredTokenException extends RuntimeException {
	public ExpiredTokenException(String message) {
		super(message);
	}
}
