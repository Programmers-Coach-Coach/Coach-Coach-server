package site.coach_coach.Coach_Coach_server.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.Coach_Coach_server.user.domain.User;
import site.coach_coach.Coach_Coach_server.user.dto.SignupDto;
import site.coach_coach.Coach_Coach_server.user.dto.UserDto;
import site.coach_coach.Coach_Coach_server.user.exception.AlreadyExistEmailException;
import site.coach_coach.Coach_Coach_server.user.exception.AlreadyExistNicknameException;
import site.coach_coach.Coach_Coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserDto signup(SignupDto signupDto) {
		if (userRepository.existsByNickname(signupDto.nickname())) {
			throw new AlreadyExistNicknameException();
		}
		if (userRepository.existsByEmail(signupDto.email())) {
			throw new AlreadyExistEmailException();
		}

		User user = buildUser(signupDto);
		userRepository.save(user);
		return UserDto.from(user);
	}

	private User buildUser(SignupDto signupDto) {
		return User.builder()
			.email(signupDto.email())
			.password(passwordEncoder.encode(signupDto.password()))
			.nickname(signupDto.nickname())
			.build();
	}
}
