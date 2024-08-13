package site.coach_coach.coach_coach_server.common.response;

public record ErrorResponse(
	Integer statusCode,
	String message
) {
}
