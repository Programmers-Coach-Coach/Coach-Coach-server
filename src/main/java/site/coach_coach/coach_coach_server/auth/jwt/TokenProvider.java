package site.coach_coach.coach_coach_server.auth.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
		Date now = new Date();
		long accessTokenExpireTime = 60 * 60 * 30 * MILLIS; // 30분
		Date expireTimeMs = new Date(now.getTime() + accessTokenExpireTime);

		return Jwts.builder()
			.setIssuer(issuer)
			.setIssuedAt(now)
			.setExpiration(expireTimeMs)
			.setSubject(user.getUserId().toString())
			.claim("nickname", user.getNickname())
			.claim("email", user.getEmail())
			.claim("token_type", "access")
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createRefreshToken(User user) {
		Date now = new Date();
		long refreshTokenExpireTime = 60 * 60 * 24 * 14L * MILLIS; // 14일
		Date expireTimeMs = new Date(now.getTime() + refreshTokenExpireTime);

		return Jwts.builder()
			.setIssuedAt(now)
			.setExpiration(expireTimeMs)
			.setSubject(user.getUserId().toString())
			.claim("token_type", "refresh")
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
		long userId = jwtParser.parseClaimsJws(token).getSubject()

	}
}
