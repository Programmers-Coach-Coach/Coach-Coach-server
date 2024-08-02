package site.coach_coach.coach_coach_server.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
	String issuer,
	String secretKey,
	Long tokenValidityInSeconds
) {

}
