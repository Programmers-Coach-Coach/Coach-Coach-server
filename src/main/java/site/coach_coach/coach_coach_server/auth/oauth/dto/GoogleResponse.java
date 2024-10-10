package site.coach_coach.coach_coach_server.auth.oauth.dto;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleResponse implements OAuth2Response {
	private final Map<String, Object> attribute;

	@Override
	public String getProvider() {
		return "google";
	}

	@Override
	public String getProviderId() {
		return attribute.get("sub").toString();
	}

	@Override
	public String getEmail() {
		return attribute.get("email").toString();
	}

	@Override
	public String getNickName() {
		return attribute.get("name").toString();
	}
}
