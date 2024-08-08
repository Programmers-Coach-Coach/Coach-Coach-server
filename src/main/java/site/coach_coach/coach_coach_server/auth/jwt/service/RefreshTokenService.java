package site.coach_coach.coach_coach_server.auth.jwt.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.domain.RefreshToken;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.repository.RefreshTokenRepository;
import site.coach_coach.coach_coach_server.user.domain.User;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void createRefreshToken(User user, String refreshToken, TokenDto tokenDto) {
		LocalDateTime expireDate = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(tokenDto.refreshTokenExpiresIn()),
			ZoneId.systemDefault()
		);

		RefreshToken newRefreshToken = RefreshToken.builder()
			.user(user)
			.refreshToken(refreshToken)
			.expireDate(expireDate)
			.build();

		refreshTokenRepository.save(newRefreshToken);
	}

	public void deleteRefreshToken(String refreshToken) {
		refreshTokenRepository.findByRefreshToken(refreshToken).ifPresent(refreshTokenRepository::delete);
	}
}
