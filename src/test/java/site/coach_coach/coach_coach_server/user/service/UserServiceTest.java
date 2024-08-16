package site.coach_coach.coach_coach_server.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.datafaker.Faker;

import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	Faker faker = new Faker();
	private SignUpRequest signUpRequest;
	private LoginRequest loginRequest;
	private User user;

	@BeforeEach
	public void setUp() {
		signUpRequest = new SignUpRequest(
			faker.name().firstName(),
			faker.internet().emailAddress(),
			faker.internet().password()
		);

		loginRequest = new LoginRequest(
			faker.internet().emailAddress(),
			faker.internet().password()
		);

		user = User.builder()
			.nickname(faker.name().firstName())
			.email(faker.internet().emailAddress())
			.password(faker.internet().password())
			.build();
	}

	@Test
	@DisplayName("회원가입 성공 시 사용자 저장")
	public void signUpSuccessTest() {
		when(userRepository.existsByNickname(signUpRequest.nickname())).thenReturn(false);
		when(userRepository.existsByEmail(signUpRequest.email())).thenReturn(false);
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

		userService.signup(signUpRequest);

		verify(userRepository).save(any(User.class));
	}

	@Test
	@DisplayName("회원가입 시 사용자 닉네임 중복 예외 처리")
	public void signUpUserAlreadyExistsTest() {
		when(userRepository.existsByNickname(signUpRequest.nickname())).thenReturn(true);

		Exception exception = assertThrows(UserAlreadyExistException.class, () -> {
			userService.signup(signUpRequest);
		});

		assertEquals(ErrorMessage.DUPLICATE_NICKNAME, exception.getMessage());
	}

	@Test
	@DisplayName("회원가입 시 이메일 중복 예외 처리")
	public void signUpEmailAlreadyExistsTest() {
		when(userRepository.existsByNickname(signUpRequest.nickname())).thenReturn(false);
		when(userRepository.existsByEmail(signUpRequest.email())).thenReturn(true);

		Exception exception = assertThrows(UserAlreadyExistException.class, () -> {
			userService.signup(signUpRequest);
		});

		assertEquals(ErrorMessage.DUPLICATE_EMAIL, exception.getMessage());
	}

	@Test
	@DisplayName("로그인 시 유저 정보 확인 성공")
	public void validateUserSuccessTest() {
		when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);

		User validateUser = userService.validateUser(loginRequest);

		assertEquals(user, validateUser);
	}

	@Test
	@DisplayName("로그인 시 이메일 정보 틀림")
	public void validateUserEmailFailTest() {
		when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.empty());

		assertThrows(InvalidUserException.class, () -> {
			userService.validateUser(loginRequest);
		});
	}

	@Test
	@DisplayName("로그인 시 비밀번호 정보 틀림")
	public void validateUserPasswordFailTest() {
		when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(false);

		assertThrows(InvalidUserException.class, () -> {
			userService.validateUser(loginRequest);
		});
	}
}
