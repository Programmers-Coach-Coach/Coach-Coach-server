package site.coach_coach.coach_coach_server.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ReviewDto(
	@NotNull Long reviewId,
	@NotBlank String userName,
	@NotNull String contents,
	@NotNull int stars,
	@NotNull String createdAt
) {
}
