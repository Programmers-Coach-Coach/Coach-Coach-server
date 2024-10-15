package site.coach_coach.coach_coach_server.auth.oauth.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
	private final Map<String, Object> attribute;
	private final Map<String, Object> kakaoAccountAttributes;
	private final Map<String, Object> profileAttributes;

	public KakaoResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
		this.kakaoAccountAttributes = (Map<String, Object>)attribute.getOrDefault("kakao_account", Map.of());
		this.profileAttributes = (Map<String, Object>)this.kakaoAccountAttributes.getOrDefault("profile", Map.of());
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getEmail() {
		return kakaoAccountAttributes.getOrDefault("email", "no-email").toString();
	}

	@Override
	public String getNickName() {
		return profileAttributes.getOrDefault("nickname", "anonymous").toString();
	}
}
