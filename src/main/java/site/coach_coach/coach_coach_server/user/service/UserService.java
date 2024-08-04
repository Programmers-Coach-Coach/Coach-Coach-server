package site.coach_coach.coach_coach_server.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.dto.UserDto;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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
		UserDto.from(user);
	}

	private User buildUser(SignUpRequest signUpRequest) {
		return User.builder()
			.email(signUpRequest.email())
			.password(passwordEncoder.encode(signUpRequest.password()))
			.nickname(signUpRequest.nickname())
			.build();
	}
}
