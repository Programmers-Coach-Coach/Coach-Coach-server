package site.coach_coach.coach_coach_server.sport.dto;

import lombok.Builder;

@Builder
public record SportDto(
	Long sportId,
	String sportName,
	String sportImageUrl
) {
}
