package site.coach_coach.coach_coach_server.common.exception;

public record ErrorResponse(
	Integer statusCode,
	String message
) {
}
