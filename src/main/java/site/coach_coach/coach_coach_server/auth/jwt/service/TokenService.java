package site.coach_coach.coach_coach_server.auth.jwt.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
public class TokenService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;

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

	public void deleteRefreshToken(Long userId, String refreshToken) {
		refreshTokenRepository.findByUser_UserIdAndRefreshToken(userId, refreshToken)
			.ifPresent(refreshTokenRepository::delete);
	}

	public String reissueAccessToken(String refreshToken) {
		if (!tokenProvider.validateRefreshToken(refreshToken)) {
			throw new InvalidTokenException(ErrorMessage.INVALID_TOKEN);
		}
		System.out.println(
			"tokenProvider.validateRefreshToken(refreshToken)" + tokenProvider.validateRefreshToken(refreshToken));

		userRepository.findByUserId(tokenProvider.getUserId(refreshToken))
			.orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER));
		System.out.println(userRepository.findByUserId(tokenProvider.getUserId(refreshToken)));

		return tokenProvider.regenerateAccessToken(refreshToken);
	}
}
