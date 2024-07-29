package site.coach_coach.coach_coach_server.coach.exception;

public class InvalidUserIdException extends RuntimeException {
	public static final String ERROR_MESSAGE = "이미 코치로 등록된 사용자입니다.";

	public InvalidUserIdException() {
		super(ERROR_MESSAGE);
	}
}
