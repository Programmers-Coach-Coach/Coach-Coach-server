package site.coach_coach.coach_coach_server.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.SignupDto;
import site.coach_coach.coach_coach_server.user.dto.UserDto;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void signup(SignupDto signupDto) {
		if (userRepository.existsByNickname(signupDto.nickname())) {
			throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_NICKNAME);
		}
		if (userRepository.existsByEmail(signupDto.email())) {
			throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_EMAIL);
		}

		User user = buildUser(signupDto);
		userRepository.save(user);
		UserDto.from(user);
	}

	private User buildUser(SignupDto signupDto) {
		return User.builder()
			.email(signupDto.email())
			.password(passwordEncoder.encode(signupDto.password()))
			.nickname(signupDto.nickname())
			.build();
	}
}
