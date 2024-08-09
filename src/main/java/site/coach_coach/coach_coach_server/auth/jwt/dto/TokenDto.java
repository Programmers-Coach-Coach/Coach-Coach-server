package site.coach_coach.coach_coach_server.auth.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TokenDto(
	@NotBlank
	String accessToken,

	@NotBlank
	Long accessTokenExpiresIn,

	@NotBlank
	String refreshToken,

	@NotBlank
	Long refreshTokenExpiresIn
) {
}
