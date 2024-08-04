package site.coach_coach.coach_coach_server.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("회원가입 성공 시 사용자 저장")
	public void testSignupSuccess() {
		SignUpRequest signUpRequest = new SignUpRequest("nickname", "email@example.com", "password");

		when(userRepository.existsByNickname("nickname")).thenReturn(false);
		when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

		userService.signup(signUpRequest);

		verify(userRepository).save(any(User.class));
	}

	@Test
	@DisplayName("회원가입 시 사용자 닉네임 중복 예외 처리")
	public void testSignupUserAlreadyExists() {
		SignUpRequest signUpRequest = new SignUpRequest("nickname", "email@example.com", "password");

		when(userRepository.existsByNickname("nickname")).thenReturn(true);

		Exception exception = assertThrows(UserAlreadyExistException.class, () -> {
			userService.signup(signUpRequest);
		});

		assertEquals(ErrorMessage.DUPLICATE_NICKNAME, exception.getMessage());
	}

	@Test
	@DisplayName("회원가입 시 이메일 중복 예외 처리")
	public void testSignupEmailAlreadyExists() {
		SignUpRequest signUpRequest = new SignUpRequest("nickname", "email@example.com", "password");

		when(userRepository.existsByNickname("nickname")).thenReturn(false);
		when(userRepository.existsByEmail("email@example.com")).thenReturn(true);

		Exception exception = assertThrows(UserAlreadyExistException.class, () -> {
			userService.signup(signUpRequest);
		});

		assertEquals(ErrorMessage.DUPLICATE_EMAIL, exception.getMessage());
	}
}
