package site.coach_coach.coach_coach_server.auth.oauth.dto;

public interface OAuth2Response {
	String getProvider();

	String getProviderId();

	String getEmail();

	String getNickName();
}
