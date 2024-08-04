// package site.coach_coach.coach_coach_server.auth.jwt;
//
// import java.util.Collections;
// import java.util.Date;
//
// import javax.crypto.SecretKey;
//
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.stereotype.Component;
//
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.JwtParser;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import site.coach_coach.coach_coach_server.auth.userDetails.CustomUserDetailsService;
// import site.coach_coach.coach_coach_server.user.domain.User;
//
// @Component
// // public class TokenProvider {
// 	public static final int MILLIS = 1000;
//
// 	private final String issuer;
// 	private final SecretKey secretKey;
// 	private final JwtParser jwtParser;
// 	private final CustomUserDetailsService customUserDetailsService;
//
// 	public TokenProvider(JwtProperties jwtProperties, CustomUserDetailsService customUserDetailsService) {
// 		byte[] secretKeyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
//
// 		this.issuer = jwtProperties.issuer();
// 		this.secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
// 		this.jwtParser = Jwts.parserBuilder()
// 			.setSigningKey(secretKey)
// 			.requireIssuer(issuer)
// 			.build();
// 		this.customUserDetailsService = customUserDetailsService;
// 	}
//
// 	public String createAccessToken(User user) {
// 		Date now = new Date();
// 		long accessTokenExpireTime = 60 * 60 * 30 * MILLIS; // 30분
// 		Date expireTimeMs = new Date(now.getTime() + accessTokenExpireTime);
//
// 		return Jwts.builder()
// 			.setIssuer(issuer)
// 			.setIssuedAt(now)
// 			.setExpiration(expireTimeMs)
// 			.setSubject(user.getUserId().toString())
// 			.claim("nickname", user.getNickname())
// 			.claim("email", user.getEmail())
// 			.claim("token_type", "access")
// 			.signWith(secretKey, SignatureAlgorithm.HS256)
// 			.compact();
// 	}
//
// 	public String createRefreshToken(User user) {
// 		Date now = new Date();
// 		long refreshTokenExpireTime = 60 * 60 * 24 * 14L * MILLIS; // 14일
// 		Date expireTimeMs = new Date(now.getTime() + refreshTokenExpireTime);
//
// 		return Jwts.builder()
// 			.setIssuedAt(now)
// 			.setExpiration(expireTimeMs)
// 			.setSubject(user.getUserId().toString())
// 			.claim("token_type", "refresh")
// 			.signWith(secretKey, SignatureAlgorithm.HS256)
// 			.compact();
// 	}
//
// 	public TokenDto generateJwt(User user) {
// 		String accessToken = createAccessToken(user);
// 		String refreshToken = createRefreshToken(user);
//
// 		return TokenDto.builder()
// 			.accessToken(accessToken)
// 			.refreshToken(refreshToken)
// 			.build();
// 	}
//
// 	public Authentication getAuthentication(String token) {
// 		String userId = getUserId(token);
//
// 		return new UsernamePasswordAuthenticationToken(userId, token, Collections.emptyList());
// 	}
//
// 	public Claims extractClaims(String token) {
// 		return jwtParser.parseClaimsJws(token).getBody();
// 	}
//
// 	public String getUserId(String token) {
// 		return extractClaims(token).getSubject();
// 	}
//
// 	public boolean validateAccessToken(String token) {
// 		// try {
// 		// 	Jws<Claims> claims = Jwts.parserBuilder()
// 		// 		.setSigningKey(secretKey).build()
// 		// 		.parseClaimsJws(token);
// 		// 	if (!claims.getBody().get("token_type").equals("access")) {
// 		// 		throw new JwtException()
// 		// 	}
// 		// } catch() {
// 		//
// 		// }
// 	}
// }
