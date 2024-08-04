package site.coach_coach.coach_coach_server.auth.jwt;

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
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;

@Component
public class TokenProvider {
	public static final int MILLIS = 1000;

	private final String issuer;
	private final SecretKey secretKey;
	private final JwtParser jwtParser;

	public TokenProvider(JwtProperties jwtProperties) {
		byte[] secretKeyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());

		this.issuer = jwtProperties.issuer();
		this.secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
		this.jwtParser = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.requireIssuer(issuer)
			.build();
	}

	public String createAccessToken(User user) {
		long accessTokenExpireTime = 60 * 60 * 30 * MILLIS; // 30분
		return createToken(user, accessTokenExpireTime, "access");
	}

	public String createRefreshToken(User user) {
		long refreshTokenExpireTime = 60 * 60 * 24 * 14L * MILLIS; // 14일
		return createToken(user, refreshTokenExpireTime, "refresh");
	}

	public String createToken(User user, long expireTime, String tokenType) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expireTime);

		return Jwts.builder()
			.setIssuer(issuer)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.setSubject(user.getUserId().toString())
			.claim("nickname", user.getNickname())
			.claim("email", user.getEmail())
			.claim("token_type", tokenType)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public TokenDto generateJwt(User user) {
		String accessToken = createAccessToken(user);
		String refreshToken = createRefreshToken(user);

		return TokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public Authentication getAuthentication(String token) {
		String userId = getUserId(token);

		return new UsernamePasswordAuthenticationToken(userId, token, Collections.emptyList());
	}

	public Claims extractClaims(String token) {
		return jwtParser.parseClaimsJws(token).getBody();
	}

	public String getUserId(String token) {
		return extractClaims(token).getSubject();
	}

	public boolean validateToken(String token, String expectedType) {
		try {
			Claims claims = extractClaims(token);

			if (!claims.get("token_type").equals(expectedType)) {
				throw new JwtException(ErrorMessage.NOT_FOUND_TOKEN.getMessage());
			}
			return !claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			throw new JwtException(ErrorMessage.EXPIRED_TOKEN.getMessage());
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			throw new JwtException(ErrorMessage.INVALID_TOKEN.getMessage());
		} catch (JwtException e) {
			throw new JwtException(e.getMessage());
		}
	}

	public boolean validateAccessToken(String token) {
		return validateToken(token, "access");
	}

	public boolean validateRefreshToken(String token) {
		return validateToken(token, "refresh");
	}
}
