package site.coach_coach.coach_coach_server.user.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import net.datafaker.Faker;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.service.TokenService;
import site.coach_coach.coach_coach_server.common.response.SuccessResponse;
import site.coach_coach.coach_coach_server.common.utils.AuthenticationUtil;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.config.CustomAuthenticationEntryPoint;
import site.coach_coach.coach_coach_server.config.SecurityConfig;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;
import site.coach_coach.coach_coach_server.user.service.UserService;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class})
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private TokenService tokenService;

	@MockBean
	private AuthenticationUtil authenticationUtil;

	private ObjectMapper objectMapper;
	Faker faker = new Faker();
	@Autowired
	private UserController userController;

	private LoginRequest loginRequest;
	private User user;
	private TokenDto tokenDto;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();

		loginRequest = new LoginRequest(
			faker.internet().emailAddress(),
			"password123!"
		);

		user = User.builder()
			.email(loginRequest.email())
			.password("encodedPassword")
			.build();

		tokenDto = new TokenDto(
			"accessToken",
			1800L,
			"refreshToken",
			3600L
		);
	}

	@Test
	@DisplayName("회원가입 성공 시 201 상태 코드 반환 및 본문 없음")
	public void signUpSuccessTest() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest(
			faker.name().firstName(),
			faker.internet().emailAddress(),
			"password123!"
		);

		doNothing().when(userService).signup(any(SignUpRequest.class));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn();

		verify(userService, times(1)).signup(signUpRequest);
		String responseContent = result.getResponse().getContentAsString();
		SuccessResponse expectedResponse = new SuccessResponse(HttpStatus.CREATED.value(), "회원가입 성공");
		assertThat(responseContent).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
	}

	@Test
	@DisplayName("회원가입 시 이메일 형식 오류로 400 상태 코드 반환 및 에러 메시지")
	public void signUpInvalidEmailTest() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest(
			faker.name().firstName(),
			"invalid-email",
			"password123!"
		);

		doNothing().when(userService).signup(any(SignUpRequest.class));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(ErrorMessage.INVALID_EMAIL);
	}

	@Test
	@DisplayName("회원가입 시 비밀번호 형식 오류로 400 상태 코드 반환 및 에러 메시지")
	public void signUpInvalidPasswordTest() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest(
			faker.name().firstName(),
			faker.internet().emailAddress(),
			"1234"
		);

		doNothing().when(userService).signup(any(SignUpRequest.class));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(ErrorMessage.INVALID_PASSWORD);
	}

	@Test
	@DisplayName("회원가입 시 닉네임 형식 오류로 400 상태 코드 반환 및 에러 메시지")
	public void signUpInvalidNicknameTest() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest(
			" test ",
			faker.internet().emailAddress(),
			"password123!"
		);

		doNothing().when(userService).signup(any(SignUpRequest.class));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(ErrorMessage.INVALID_NICKNAME);
	}

	@Test
	@DisplayName("회원가입 시 닉네임 중복으로 409 상태 코드 반환 및 에러 메시지")
	public void signUpNicknameConflictTest() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest(
			faker.name().firstName(),
			faker.internet().emailAddress(),
			"password123!"
		);

		doThrow(new UserAlreadyExistException(ErrorMessage.DUPLICATE_NICKNAME))
			.when(userService).signup(signUpRequest);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isConflict())
			.andReturn();

		verify(userService, times(1)).signup(signUpRequest);
		assertThat(result.getResponse().getContentAsString()).contains(ErrorMessage.DUPLICATE_NICKNAME);
	}

	@Test
	@DisplayName("회원가입 시 이메일 중복으로 409 상태 코드 반환 및 에러 메시지")
	public void signUpEmailConflictTest() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest(
			faker.name().firstName(),
			faker.internet().emailAddress(),
			"password123!"
		);

		doThrow(new UserAlreadyExistException(ErrorMessage.DUPLICATE_EMAIL))
			.when(userService).signup(signUpRequest);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
			.andExpect(MockMvcResultMatchers.status().isConflict())
			.andReturn();

		verify(userService, times(1)).signup(signUpRequest);
		assertThat(result.getResponse().getContentAsString()).contains(ErrorMessage.DUPLICATE_EMAIL);
	}

	@Test
	@DisplayName("로그인 성공 시 200 상태 코드 반환 및 토큰 쿠키 설정")
	public void loginSuccessTest() throws Exception {
		when(userService.validateUser(loginRequest)).thenReturn(user);
		when(userService.createJwt(user)).thenReturn(tokenDto);
		when(tokenProvider.createCookie(eq("access_token"), anyString())).thenReturn(
			new Cookie("access_token", tokenDto.accessToken()));
		when(tokenProvider.createCookie(eq("refresh_token"), anyString())).thenReturn(
			new Cookie("refresh_token", tokenDto.refreshToken()));
		doNothing().when(tokenService).createRefreshToken(user, tokenDto.refreshToken(), tokenDto);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.cookie().exists("access_token"))
			.andExpect(MockMvcResultMatchers.cookie().exists("refresh_token"))
			.andReturn();

		verify(userService, times(1)).validateUser(loginRequest);
		verify(userService, times(1)).createJwt(user);
		verify(tokenProvider, times(1)).createCookie("access_token", tokenDto.accessToken());
		verify(tokenProvider, times(1)).createCookie("refresh_token", tokenDto.refreshToken());
		verify(tokenService, times(1)).createRefreshToken(user, tokenDto.refreshToken(), tokenDto);
	}

	@Test
	@DisplayName("로그인 시 회원 정보가 다를 경우 401 상태 코드 반환 및 에러 메시지")
	public void loginInvalidEmailTest() throws Exception {
		when(userService.validateUser(loginRequest)).thenThrow(new InvalidUserException());

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(MockMvcResultMatchers.status().isUnauthorized())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(ErrorMessage.INVALID_USER);
	}
}
