package site.coach_coach.coach_coach_server.user.controller;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.service.RefreshTokenService;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.config.SecurityConfig;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;
import site.coach_coach.coach_coach_server.user.service.UserService;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private RefreshTokenService refreshTokenService;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	@DisplayName("회원가입 성공 시 201 상태 코드 반환 및 본문 없음")
	public void testSignupSuccess() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("test", "test@test.com", "password123!");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.content().string(""));
	}

	@Test
	@DisplayName("회원가입 시 이메일 형식 오류로 400 상태 코드 반환 및 에러 메시지")
	public void testSignupInvalidEmail() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("test", "invalid-email", "password123!");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorMessage.INVALID_EMAIL));
	}

	@Test
	@DisplayName("회원가입 시 비밀번호 형식 오류로 400 상태 코드 반환 및 에러 메시지")
	public void testSignupInvalidPassword() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("test", "test@test.com", "short");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorMessage.INVALID_PASSWORD));
	}

	@Test
	@DisplayName("회원가입 시 닉네임 형식 오류로 400 상태 코드 반환 및 에러 메시지")
	public void testSignupInvalidNickname() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest(" test ", "test@test.com", "password123!");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorMessage.INVALID_NICKNAME));
	}

	@Test
	@DisplayName("회원가입 시 닉네임 중복으로 409 상태 코드 반환 및 에러 메시지")
	public void testSignupNicknameConflict() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("nickname", "test@test.com", "password123!");

		doThrow(new UserAlreadyExistException(ErrorMessage.DUPLICATE_NICKNAME))
			.when(userService).signup(signUpRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isConflict())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorMessage.DUPLICATE_NICKNAME));
	}

	@Test
	@DisplayName("회원가입 시 이메일 중복으로 409 상태 코드 반환 및 에러 메시지")
	public void testSignupEmailConflict() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest("nickname", "existing@example.com", "password123!");

		doThrow(new UserAlreadyExistException(ErrorMessage.DUPLICATE_EMAIL))
			.when(userService).signup(signUpRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isConflict())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorMessage.DUPLICATE_EMAIL));
	}
}
