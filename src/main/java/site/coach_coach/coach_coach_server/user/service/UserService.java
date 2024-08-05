package site.coach_coach.coach_coach_server.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.exception.IncorrectPasswordException;
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
			throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_NICKNAME.getMessage());
		}
		if (userRepository.existsByEmail(signUpRequest.email())) {
			throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_EMAIL.getMessage());
		}

		User user = buildUser(signUpRequest);
		userRepository.save(user);
	}

	public TokenDto login(LoginRequest loginRequest) throws IncorrectPasswordException {
		String email = loginRequest.email();
		String password = loginRequest.password();
		User user = userRepository.findByEmail(email)
			.orElseThrow(UserNotFoundException::new);

		boolean isIncorrectPassword = !passwordEncoder.matches(password, user.getPassword());
		if (isIncorrectPassword) {
			throw new IncorrectPasswordException();
		}

		return tokenProvider.generateJwt(user);
	}

	private User buildUser(SignUpRequest signUpRequest) {
		return User.builder()
			.email(signUpRequest.email())
			.password(passwordEncoder.encode(signUpRequest.password()))
			.nickname(signUpRequest.nickname())
			.build();
	}
}
