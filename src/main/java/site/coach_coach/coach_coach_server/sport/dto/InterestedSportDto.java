package site.coach_coach.coach_coach_server.sport.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record InterestedSportDto(
	@NotBlank
	Long userId,

	@NotBlank
	String sportName
) {
}
