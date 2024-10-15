package site.coach_coach.coach_coach_server.auth.oauth;

import java.util.Map;

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
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("oauth2 user: {}", oAuth2User);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = parseOAuth2Response(registrationId, oAuth2User.getAttributes());
		if (oAuth2Response == null) {
			throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
		}

		User user = findOrRegisterUser(oAuth2Response);
		return createCustomOAuth2User(user, oAuth2Response);
	}

	private OAuth2Response parseOAuth2Response(String provider, Map<String, Object> attributes) {
		return switch (provider) {
			case "google" -> new GoogleResponse(attributes);
			case "kakao" -> new KakaoResponse(attributes);
			default -> null;
		};
	}

	private User findOrRegisterUser(OAuth2Response response) {
		String username = response.getProvider() + " " + response.getProviderId();

		return userRepository.findByUsername(username)
			.orElseGet(() -> createUser(response, username));
	}

	private User createUser(OAuth2Response response, String username) {
		if (userRepository.existsByEmail(response.getEmail())) {
			throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_NICKNAME);
		}

		User user = new User();
		user.signUpOAuth2(
			response.getNickName(),
			response.getEmail(),
			username
		);
		return userRepository.save(user);
	}

	private CustomOAuth2User createCustomOAuth2User(User user, OAuth2Response response) {
		OAuth2UserDto dto = new OAuth2UserDto(response.getNickName(), user.getUsername());
		return new CustomOAuth2User(dto, user);
	}
}
