package site.coach_coach.coach_coach_server.auth.oauth.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
	private final OAuth2UserDto oAuth2UserDto;

	public CustomOAuth2User(OAuth2UserDto oAuth2UserDto) {
		this.oAuth2UserDto = oAuth2UserDto;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getName() {
		return oAuth2UserDto.name();
	}

	public String getUsername() {
		return oAuth2UserDto.username();
	}
}
