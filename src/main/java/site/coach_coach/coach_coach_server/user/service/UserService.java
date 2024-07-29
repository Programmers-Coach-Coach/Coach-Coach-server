package site.coach_coach.coach_coach_server.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.dto.StartedAsCoachDto;
import site.coach_coach.coach_coach_server.coach.service.CoachService;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.SignupDto;
import site.coach_coach.coach_coach_server.user.dto.UserDto;
import site.coach_coach.coach_coach_server.user.exception.AlreadyExistEmailException;
import site.coach_coach.coach_coach_server.user.exception.AlreadyExistNicknameException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CoachService coachService;

	@Transactional
	public void signup(SignupDto signupDto) {
		if (userRepository.existsByNickname(signupDto.nickname())) {
			throw new AlreadyExistNicknameException();
		}
		if (userRepository.existsByEmail(signupDto.email())) {
			throw new AlreadyExistEmailException();
		}

		User user = buildUser(signupDto);
		userRepository.save(user);

		if (signupDto.isCoach()) {
			coachService.startedAsCoach(new StartedAsCoachDto(user.getUserId()));
		}
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
