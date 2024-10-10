package site.coach_coach.coach_coach_server.auth.oauth.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
	private final Map<String, Object> attribute;
	private final Map<String, Object> kakaoAccountAttributes;
	private final Map<String, Object> profileAttributes;

	public KakaoResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
		this.kakaoAccountAttributes = (Map<String, Object>)attribute.get("kakao_account");
		this.profileAttributes = (Map<String, Object>)attribute.get("profile");
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
		return kakaoAccountAttributes.get("email").toString();
	}

	@Override
	public String getNickName() {
		return profileAttributes.get("nickname").toString();
	}
}
