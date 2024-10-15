package site.coach_coach.coach_coach_server.auth.jwt;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.ResponseCookie;
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
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetailsService;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;

@Slf4j
@Component
public class TokenProvider {
	public static final int MILLIS = 1000;

	private final String issuer;
	private final SecretKey secretKey;
	private final long accessTokenExpireTime;
	private final long refreshTokenExpireTime;
	private final JwtParser jwtParser;
	private final CustomUserDetailsService customUserDetailsService;

	public TokenProvider(JwtProperties jwtProperties, CustomUserDetailsService customUserDetailsService) {
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
			.claim("role", user.getRole())
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

	public ResponseCookie createCookie(String name, String value, String domain) {
		long maxAge;
		if (name.equals("access_token")) {
			maxAge = accessTokenExpireTime;
		} else if (name.equals("refresh_token")) {
			maxAge = refreshTokenExpireTime;
		} else {
			return null;
		}

		return ResponseCookie.from(name, value)
			.domain(domain)
			.path("/")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.maxAge((maxAge / MILLIS))
			.build();
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

	public String getRoleFromJwt(String token) {
		Claims claims = extractClaims(token);
		return claims.get("role", String.class);
	}

	public boolean validateToken(String token, String type) {
		try {
			Claims claims = extractClaims(token);
			Object tokenType = claims.get("token_type");
			if (tokenType == null || !tokenType.equals(type) || token == null) {
				log.error("Not Found Token.");
				throw new JwtException(ErrorMessage.NOT_FOUND_TOKEN);
			}
			return !claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			log.error("Handled exception: [{}] - {}", e.getClass().getSimpleName(), e.getMessage());
			throw new JwtException(ErrorMessage.EXPIRED_TOKEN);
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			log.error("Handled exception: [{}] - {}", e.getClass().getSimpleName(), e.getMessage());
			throw new JwtException(ErrorMessage.INVALID_TOKEN);
		} catch (JwtException e) {
			log.warn("Unhandled exception: [{}] - {}", e.getClass().getSimpleName(), e.getMessage());
			throw new JwtException(e.getMessage());
		}
	}

	public boolean validateAccessToken(String token) {
		return validateToken(token, "access_token");
	}

	public boolean validateRefreshToken(String token) {
		return validateToken(token, "refresh_token");
	}

	public String regenerateAccessToken(String refreshToken) {
		Long userId = getUserId(refreshToken);
		CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(userId));
		User user = userDetails.getUser();

		return createAccessToken(user);
	}

	public ResponseCookie clearCookie(String type, String domain) {
		return ResponseCookie.from(type, "")
			.domain(domain)
			.path("/")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.maxAge(0)
			.build();
	}
}
