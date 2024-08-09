package site.coach_coach.coach_coach_server.user.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;
import site.coach_coach.coach_coach_server.user.exception.UserNotFoundException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;

	@Transactional
	public void signup(SignUpRequest signUpRequest) {
		if (userRepository.existsByNickname(signUpRequest.nickname())) {
			throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_NICKNAME);
		}
		if (userRepository.existsByEmail(signUpRequest.email())) {
			throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_EMAIL);
		}

		User user = buildUser(signUpRequest);
		userRepository.save(user);
	}

	public User validateUser(LoginRequest loginRequest) {
		String email = loginRequest.email();
		String password = loginRequest.password();
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new UserNotFoundException();
		}

		return user;
	}

	public TokenDto createJwt(User user) {
		return tokenProvider.generateJwt(user);
	}

	public String logout(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = tokenProvider.getCookieValue(request, "access_token");
		String refreshToken = tokenProvider.getCookieValue(request, "refresh_token");

		System.out.println("확인 시작");

		if (accessToken != null && tokenProvider.validateAccessToken(accessToken)) {
			System.out.println("accesstoken 삭제");
			tokenProvider.clearCookie(response, "access_token");
		}
		System.out.println("accesstoken validation " + tokenProvider.validateAccessToken(accessToken));

		if (refreshToken != null && tokenProvider.validateRefreshToken(refreshToken)) {
			System.out.println("refreshtoken 삭제");
			tokenProvider.clearCookie(response, "refresh_token");
		}
		System.out.println("refreshtoken validation " + tokenProvider.validateRefreshToken(refreshToken));

		SecurityContextHolder.clearContext();
		return refreshToken;
	}

	private User buildUser(SignUpRequest signUpRequest) {
		return User.builder()
			.email(signUpRequest.email())
			.password(passwordEncoder.encode(signUpRequest.password()))
			.nickname(signUpRequest.nickname())
			.build();
	}
}
