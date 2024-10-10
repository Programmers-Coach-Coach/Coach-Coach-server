package site.coach_coach.coach_coach_server.auth.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.auth.oauth.dto.CustomOAuth2User;
import site.coach_coach.coach_coach_server.auth.oauth.dto.GoogleResponse;
import site.coach_coach.coach_coach_server.auth.oauth.dto.KakaoResponse;
import site.coach_coach.coach_coach_server.auth.oauth.dto.OAuth2Response;
import site.coach_coach.coach_coach_server.auth.oauth.dto.OAuth2UserDto;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;
import site.coach_coach.coach_coach_server.user.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;
	private final UserService userService;

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
		User existUser = userRepository.findByUsername(username);
		if (existUser == null) {
			userService.checkEmailDuplicate(oAuth2Response.getEmail());
			userService.checkNicknameDuplicate(oAuth2Response.getNickName());
			User user = new User();
			user.signUpOAuth2(
				oAuth2Response.getNickName(),
				oAuth2Response.getEmail(),
				username
			);
			userRepository.save(user);

			OAuth2UserDto oauth2UserDto = new OAuth2UserDto(oAuth2Response.getNickName(), username);
			return new CustomOAuth2User(oauth2UserDto, user);
		} else {
			existUser.updateOAuth2UserInfo(
				oAuth2Response.getNickName(),
				oAuth2Response.getEmail()
			);
			userRepository.save(existUser);
			OAuth2UserDto oauth2UserDto = new OAuth2UserDto(oAuth2Response.getNickName(), username);
			return new CustomOAuth2User(oauth2UserDto, existUser);
		}
	}
}
