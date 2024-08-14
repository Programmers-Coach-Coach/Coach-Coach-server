package site.coach_coach.coach_coach_server.auth.jwt.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.exception.InvalidTokenException;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.domain.RefreshToken;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.repository.RefreshTokenRepository;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;

	public void createRefreshToken(User user, String refreshToken, TokenDto tokenDto) {
		LocalDateTime expireDate = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(tokenDto.refreshTokenExpiresIn()),
			ZoneId.systemDefault()
		);

		RefreshToken newRefreshToken = RefreshToken.builder()
			.userId(user.getUserId())
			.refreshToken(refreshToken)
			.expireDate(expireDate)
			.build();

		refreshTokenRepository.save(newRefreshToken);
	}

	public boolean existsRefreshToken(String refreshToken) {
		if (refreshTokenRepository.existsByRefreshToken(refreshToken)) {
			return true;
		}
		throw new JwtException(ErrorMessage.NOT_FOUND_TOKEN);
	}

	public void deleteRefreshToken(Long userId, String refreshToken) {
		RefreshToken token = refreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken)
			.orElseThrow(() -> new JwtException(ErrorMessage.NOT_FOUND_TOKEN));

		refreshTokenRepository.delete(token);
	}

	public String reissueAccessToken(String refreshToken) {
		if (!tokenProvider.validateRefreshToken(refreshToken) || !existsRefreshToken(refreshToken)) {
			throw new InvalidTokenException(ErrorMessage.INVALID_TOKEN);
		}

		userRepository.findByUserId(tokenProvider.getUserId(refreshToken))
			.orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER));

		return tokenProvider.regenerateAccessToken(refreshToken);
	}
}
