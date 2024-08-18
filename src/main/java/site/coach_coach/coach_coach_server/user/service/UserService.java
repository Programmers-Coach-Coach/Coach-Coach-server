package site.coach_coach.coach_coach_server.user.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.coach.exception.NotFoundSportException;
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

	@Transactional
	public void updateUserProfile(Long userId, MultipartFile profileImage, UserProfileRequest userProfileRequest) throws
		IOException {
		User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
		String nickname = user.getNickname();
		if (!Objects.equals(nickname, userProfileRequest.nickname())) {
			if (userRepository.existsByNickname(userProfileRequest.nickname())) {
				throw new UserAlreadyExistException(ErrorMessage.DUPLICATE_NICKNAME);
			}
			nickname = userProfileRequest.nickname();
		}

		String profileImageUrl = user.getProfileImageUrl();

		if (profileImage != null) {
			profileImageUrl = amazonS3Uploader.uploadMultipartFile(profileImage, "users/" + userId);
		}

		List<InterestedSport> interestedSports = user.getInterestedSports();
		if (userProfileRequest.interestedSports() != null) {
			interestedSportRepository.deleteByUser(user);

			interestedSports = userProfileRequest.interestedSports().stream().map(
				interestedSport -> {
					Sport sport = sportRepository.findBySportName(interestedSport.sportName())
						.orElseThrow(() -> new NotFoundSportException(ErrorMessage.NOT_FOUND_SPORTS));
					return InterestedSport.builder()
						.user(user)
						.sport(sport)
						.build();
				}).collect(Collectors.toList());
			interestedSportRepository.saveAll(interestedSports);
		}

		user.updateProfile(
			nickname,
			profileImageUrl,
			userProfileRequest.gender() != null ? userProfileRequest.gender() : user.getGender(),
			userProfileRequest.localAddress() != null ? userProfileRequest.localAddress() : user.getLocalAddress(),
			userProfileRequest.localAddressDetail() != null ? userProfileRequest.localAddressDetail() :
				user.getLocalAddressDetail(),
			userProfileRequest.introduction() != null ? userProfileRequest.introduction() : user.getIntroduction(),
			interestedSports
		);

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
