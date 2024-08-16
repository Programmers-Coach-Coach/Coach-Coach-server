package site.coach_coach.coach_coach_server.sport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SportDto(
	@NotBlank
	Long sportId,

	@NotBlank
	String sportName,

	@NotBlank
	@Size(max = 500)
	String sportImageUrl
) {
}
