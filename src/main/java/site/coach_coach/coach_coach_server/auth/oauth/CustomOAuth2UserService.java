package site.coach_coach.coach_coach_server.auth.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.auth.oauth.dto.CustomOAuth2User;
import site.coach_coach.coach_coach_server.auth.oauth.dto.GoogleResponse;
import site.coach_coach.coach_coach_server.auth.oauth.dto.KakaoResponse;
import site.coach_coach.coach_coach_server.auth.oauth.dto.OAuth2Response;
import site.coach_coach.coach_coach_server.auth.oauth.dto.OAuth2UserDto;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("oauth2 user: {}", oAuth2User);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;
		if (registrationId.equals("google")) {
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}

		String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

		OAuth2UserDto oAuth2UserDto = new OAuth2UserDto(oAuth2Response.getName(), username);

		return new CustomOAuth2User(oAuth2UserDto);
	}
}
