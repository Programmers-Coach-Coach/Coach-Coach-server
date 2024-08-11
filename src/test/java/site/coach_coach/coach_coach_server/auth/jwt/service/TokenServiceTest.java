package site.coach_coach.coach_coach_server.auth.jwt.service;

import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.datafaker.Faker;

import site.coach_coach.coach_coach_server.auth.jwt.domain.RefreshToken;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.repository.RefreshTokenRepository;
import site.coach_coach.coach_coach_server.user.domain.User;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@InjectMocks
	private TokenService tokenService;

	private User user;
	private TokenDto tokenDto;

	Faker faker = new Faker();

	private RefreshToken refreshToken;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.nickname(faker.name().firstName())
			.email(faker.internet().emailAddress())
			.password("password123!")
			.build();
		tokenDto = new TokenDto(
			"accessToken",
			1800L,
			"refreshToken",
			3600L
		);
		refreshToken = RefreshToken.builder()
			.user(user)
			.refreshToken(tokenDto.refreshToken())
			.expireDate(LocalDateTime.now().plusDays(1))
			.build();
	}

	@Test
	@DisplayName("리프레시 토큰 db에 저장")
	public void createRefreshTokenTest() {
		tokenService.createRefreshToken(user, tokenDto.refreshToken(), tokenDto);

		LocalDateTime expectedExpireDate = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(tokenDto.refreshTokenExpiresIn()),
			ZoneId.systemDefault()
		);

		verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
	}
}
