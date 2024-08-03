package site.coach_coach.coach_coach_server.auth.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import site.coach_coach.coach_coach_server.user.domain.User;

@Component
public class TokenProvider {
	public static final int MILLIS = 1000;

	private final long tokenExpiresInMillis;
	private final String issuer;
	private final SecretKey secretKey;
	private final JwtParser jwtParser;

	public TokenProvider(JwtProperties jwtProperties) {
		byte[] secretKeyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());

		this.tokenExpiresInMillis = jwtProperties.tokenExpiredInSeconds() * MILLIS;
		this.issuer = jwtProperties.issuer();
		this.secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
		this.jwtParser = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.requireIssuer(issuer)
			.build();
	}

	public String createToken(User user) {
		Date now = new Date();
		Date expireTimeMs = new Date(now.getTime() + tokenExpiresInMillis);

		return Jwts.builder()
			.setIssuer(issuer)
			.setIssuedAt(now)
			.setExpiration(expireTimeMs)
			.setSubject(user.getUserId().toString())
			.claim("nickname", user.getNickname())
			.claim("email", user.getEmail())
			.compact();
	}
}
