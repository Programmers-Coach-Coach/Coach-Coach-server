package site.coach_coach.coach_coach_server.coach.dto;

import jakarta.validation.constraints.NotNull;

public record CreateContactResponse(
	@NotNull
	int statusCode,

	@NotNull
	Long matchingId
) {
}
