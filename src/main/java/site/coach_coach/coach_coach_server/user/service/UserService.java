package site.coach_coach.coach_coach_server.user.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.utils.AmazonS3Uploader;
import site.coach_coach.coach_coach_server.sport.domain.InterestedSport;
import site.coach_coach.coach_coach_server.sport.domain.Sport;
import site.coach_coach.coach_coach_server.sport.repository.InterestedSportRepository;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.PasswordRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.dto.UserProfileRequest;
import site.coach_coach.coach_coach_server.user.dto.UserProfileResponse;
import site.coach_coach.coach_coach_server.user.exception.IncorrectPasswordException;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final InterestedSportRepository interestedSportRepository;
	private final AmazonS3Uploader amazonS3Uploader;
	private final SportRepository sportRepository;

	public void checkNicknameDuplicate(String nickname) {
		if (userRepository.existsByNickname(nickname)) {
			throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_NICKNAME);
		}
	}

	public void checkEmailDuplicate(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_EMAIL);
		}
	}

	public void signup(SignUpRequest signUpRequest) {
		checkNicknameDuplicate(signUpRequest.nickname());
		checkEmailDuplicate(signUpRequest.email());
		User user = buildUserForSignUp(signUpRequest);
		userRepository.save(user);
	}

	public User validateUser(LoginRequest loginRequest) {
		String email = loginRequest.email();
		String password = loginRequest.password();
		User user = userRepository.findByEmail(email).orElseThrow(InvalidUserException::new);

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new InvalidUserException();
		}

		return user;
	}

	public TokenDto createJwt(User user) {
		return tokenProvider.generateJwt(user);
	}

	public void validatePassword(Long userId, PasswordRequest passwordRequest) {
		User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
		if (!passwordEncoder.matches(passwordRequest.password(), user.getPassword())) {
			throw new IncorrectPasswordException();
		}
	}

	public UserProfileResponse getUserProfile(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
		return UserProfileResponse.from(user);
	}

	public void updateUserProfile(Long userId, UserProfileRequest userProfileRequest) throws IOException {
		User existingUser = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
		if (!Objects.equals(existingUser.getNickname(), userProfileRequest.nickname())) {
			if (userRepository.existsByNickname(userProfileRequest.nickname())) {
				throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_NICKNAME);
			}
		}

		String profileImageUrl = amazonS3Uploader.uploadMultipartFile(userProfileRequest.profileImage(), "users/");

		interestedSportRepository.deleteByUser(existingUser);

		List<InterestedSport> interestedSports = userProfileRequest.interestedSports().stream().map(interestedSport -> {
			Sport sport = sportRepository.findBySportName(interestedSport.getSport().getSportName())
				.orElseThrow(() -> new InvalidUserException());

			return InterestedSport.builder()
				.user(existingUser)
				.sport(sport)
				.build();
		}).collect(Collectors.toList());
		interestedSportRepository.saveAll(interestedSports);

		User user = User.builder()
			.userId(userId)
			.nickname(userProfileRequest.nickname())
			.email(existingUser.getEmail())
			.password(existingUser.getPassword())
			.profileImageUrl(profileImageUrl)
			.gender(userProfileRequest.gender())
			.localAddress(userProfileRequest.localAddress())
			.localAddressDetail(userProfileRequest.localAddressDetail())
			.introduction(userProfileRequest.introduction())
			.interestedSports(interestedSports)
			.build();
		userRepository.save(user);
	}

	private User buildUserForSignUp(SignUpRequest signUpRequest) {
		return User.builder()
			.email(signUpRequest.email())
			.password(passwordEncoder.encode(signUpRequest.password()))
			.nickname(signUpRequest.nickname())
			.build();
	}
}
