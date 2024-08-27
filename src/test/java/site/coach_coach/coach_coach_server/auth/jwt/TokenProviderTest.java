package site.coach_coach.coach_coach_server.auth.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import net.datafaker.Faker;

import io.jsonwebtoken.Claims;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetailsService;
import site.coach_coach.coach_coach_server.user.domain.User;

@TestPropertySource(locations = "/application.properties")
@SpringBootTest
public class TokenProviderTest {
	@Autowired
	private JwtProperties jwtProperties;

	@MockBean
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private TokenProvider tokenProvider;

	Faker faker = new Faker();
	private User user;

	@BeforeEach
	public void setUp() {
		user = new User(
			faker.number().randomNumber(),
			faker.name().firstName(),
			faker.internet().emailAddress(),
			"test1234!",
			null, null, null, null, null, null, null, null, null, null
		);
		tokenProvider = new TokenProvider(jwtProperties, customUserDetailsService);
	}

	@Test
	@DisplayName("Access Token 생성 테스트")
	public void createAccessTokenTest() {
		String accessToken = tokenProvider.createAccessToken(user);
		assertThat(accessToken).isNotNull();

		Claims claims = tokenProvider.extractClaims(accessToken);

		assertThat(claims.getSubject()).isEqualTo(user.getUserId().toString());
		assertThat(claims.get("nickname")).isEqualTo(user.getNickname());
		assertThat(claims.get("email")).isEqualTo(user.getEmail());
		assertThat(claims.get("token_type")).isEqualTo("access_token");
		assertThat(claims.getExpiration()).isAfter(new Date());
	}

	@Test
	@DisplayName("Refresh Token 생성 테스트")
	public void createRefreshTokenTest() {
		String refreshToken = tokenProvider.createRefreshToken(user);
		assertThat(refreshToken).isNotNull();

		Claims claims = tokenProvider.extractClaims(refreshToken);
		assertThat(claims.getSubject()).isEqualTo(user.getUserId().toString());
		assertThat(claims.get("token_type")).isEqualTo("refresh_token");
		assertThat(claims.getExpiration()).isAfter(new Date());
	}

	@Test
	@DisplayName("TokenDto 생성 테스트")
	public void generateJwtTest() {
		TokenDto tokenDto = tokenProvider.generateJwt(user);
		assertThat(tokenDto).isNotNull();
		assertThat(tokenDto.accessToken()).isNotNull();
		assertThat(tokenDto.refreshToken()).isNotNull();
		assertThat(tokenDto.accessTokenExpiresIn()).isGreaterThan(System.currentTimeMillis());
		assertThat(tokenDto.refreshTokenExpiresIn()).isGreaterThan(System.currentTimeMillis());
	}

	@Test
	@DisplayName("Authentication 생성 테스트")
	public void getAuthenticationTest() {
		CustomUserDetails userDetails = mock(CustomUserDetails.class);
		when(customUserDetailsService.loadUserByUsername(user.getUserId().toString())).thenReturn(userDetails);
		Authentication authentication = tokenProvider.getAuthentication(tokenProvider.createAccessToken(user));
		assertThat(Collections.singletonList(authentication)).isNotNull();
		assertThat((authentication).getPrincipal()).isEqualTo(userDetails);
	}

	@Test
	@DisplayName("Access Token 재생성 테스트")
	public void regenerateAccessTokenTest() {
		String refreshToken = tokenProvider.createRefreshToken(user);
		when(customUserDetailsService.loadUserByUsername(user.getUserId().toString()))
			.thenReturn(new CustomUserDetails(user));

		String newAccessToken = tokenProvider.regenerateAccessToken(refreshToken);
		assertThat(newAccessToken).isNotNull();
		Claims claims = tokenProvider.extractClaims(newAccessToken);
		assertThat(claims.getSubject()).isEqualTo(user.getUserId().toString());
		assertThat(claims.get("token_type")).isEqualTo("access_token");
		assertThat(claims.getExpiration()).isAfter(new Date());
	}
}
