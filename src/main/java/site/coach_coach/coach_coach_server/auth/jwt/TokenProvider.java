package site.coach_coach.coach_coach_server.auth.jwt;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.repository.RefreshTokenRepository;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetailsService;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;

@Component
public class TokenProvider {
	public static final int MILLIS = 1000;

	private final String issuer;
	private final SecretKey secretKey;
	private final long accessTokenExpireTime;
	private final long refreshTokenExpireTime;
	private final JwtParser jwtParser;
	private final CustomUserDetailsService customUserDetailsService;

	private final RefreshTokenRepository refreshTokenRepository;

	public TokenProvider(JwtProperties jwtProperties, CustomUserDetailsService customUserDetailsService,
		RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
		byte[] secretKeyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());

		this.issuer = jwtProperties.issuer();
		this.secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
		this.accessTokenExpireTime = jwtProperties.accessTokenExpireTime() * MILLIS;
		this.refreshTokenExpireTime = jwtProperties.refreshTokenExpireTIme() * MILLIS;
		this.jwtParser = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.requireIssuer(issuer)
			.build();
		this.customUserDetailsService = customUserDetailsService;
	}

	public String createAccessToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + accessTokenExpireTime);

		return Jwts.builder()
			.setIssuer(issuer)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.setSubject(user.getUserId().toString())
			.claim("nickname", user.getNickname())
			.claim("email", user.getEmail())
			.claim("token_type", "access_token")
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createRefreshToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + refreshTokenExpireTime);

		return Jwts.builder()
			.setIssuer(issuer)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.setSubject(user.getUserId().toString())
			.claim("token_type", "refresh_token")
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public TokenDto generateJwt(User user) {
		String accessToken = createAccessToken(user);
		String refreshToken = createRefreshToken(user);

		long currentMillis = System.currentTimeMillis();

		return TokenDto.builder()
			.accessToken(accessToken)
			.accessTokenExpiresIn(currentMillis + accessTokenExpireTime)
			.refreshToken(refreshToken)
			.refreshTokenExpiresIn(currentMillis + refreshTokenExpireTime)
			.build();
	}

	public Cookie createCookie(String name, String value) {
		long maxAge;
		if (name.equals("access_token")) {
			maxAge = accessTokenExpireTime;
		} else if (name.equals("refresh_token")) {
			maxAge = refreshTokenExpireTime;
		} else {
			return null;
		}

		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge((int)(maxAge / 1000));
		return cookie;
	}

	public String getCookieValue(HttpServletRequest request, String type) {
		if (request == null || type == null) {
			throw new IllegalArgumentException();
		}

		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		return Arrays.stream(cookies)
			.filter(cookie -> type.equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
	}

	public Authentication getAuthentication(String token) {
		CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(getUserId(token).toString());

		return new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList());
	}

	public Claims extractClaims(String token) {
		return jwtParser.parseClaimsJws(token).getBody();
	}

	public Long getUserId(String token) {
		return Long.parseLong(extractClaims(token).getSubject());
	}

	public boolean validateToken(String token, String type) {
		try {
			Claims claims = extractClaims(token);
			if (!claims.get("token_type").equals(type)) {
				throw new JwtException(ErrorMessage.NOT_FOUND_TOKEN);
			}
			return !claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			throw new JwtException(ErrorMessage.EXPIRED_TOKEN);
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			throw new JwtException(ErrorMessage.INVALID_TOKEN);
		} catch (JwtException e) {
			throw new JwtException(e.getMessage());
		}
	}

	public boolean validateAccessToken(String token) {
		return validateToken(token, "access_token");
	}

	public boolean validateRefreshToken(String token) {
		return validateToken(token, "refresh_token");
	}

	public boolean existsRefreshToken(String refreshToken) {
		return refreshTokenRepository.existsByRefreshToken(refreshToken);
	}

	public String regenerateAccessToken(String refreshToken) {
		String userId = extractClaims(refreshToken).getSubject();
		CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
		User user = userDetails.getUser();

		return createAccessToken(user);
	}

	public void clearCookie(HttpServletResponse response, String type) {
		Cookie oldCookie = new Cookie(type, null);
		oldCookie.setHttpOnly(true);
		oldCookie.setPath("/");
		oldCookie.setMaxAge(0);
		response.addCookie(oldCookie);
	}
}
