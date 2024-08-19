package site.coach_coach.coach_coach_server.user.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

import jakarta.servlet.http.HttpServletRequest;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.service.TokenService;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.constants.SuccessMessage;
import site.coach_coach.coach_coach_server.common.response.SuccessResponse;
import site.coach_coach.coach_coach_server.common.utils.AmazonS3Uploader;
import site.coach_coach.coach_coach_server.config.CustomAuthenticationEntryPoint;
import site.coach_coach.coach_coach_server.config.SecurityConfig;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.PasswordRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.dto.UserProfileResponse;
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
	private AmazonS3Uploader amazonS3Uploader;

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

		User user = User.builder()
			.email(loginRequest.email())
			.password("encodedPassword")
			.build();

		TokenDto tokenDto = new TokenDto(
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
		SuccessResponse expectedResponse = new SuccessResponse(HttpStatus.CREATED.value(),
			SuccessMessage.SIGNUP_SUCCESS.getMessage());
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
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

		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
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
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
		assertThat(result.getResponse().getContentAsString()).contains(ErrorMessage.DUPLICATE_EMAIL);
	}

	@Test
	@DisplayName("로그인 성공 테스트")
	public void loginSuccessTest() throws Exception {
		given(userService.validateUser(any(LoginRequest.class))).willReturn(user);
		given(userService.createJwt(any(User.class))).willReturn(tokenDto);

		// when
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// then
		verify(userService, times(1)).validateUser(any(LoginRequest.class));
		verify(userService, times(1)).createJwt(any(User.class));
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(result.getResponse().getContentAsString()).contains(SuccessMessage.LOGIN_SUCCESS.getMessage());
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

	@Test
	@DisplayName("로그아웃 성공 테스트")
	public void logoutSuccessTest() throws Exception {
		// given
		CustomUserDetails userDetails = new CustomUserDetails(user);
		String refreshToken = "refreshToken";
		given(tokenProvider.getCookieValue(any(HttpServletRequest.class), eq("refresh_token"))).willReturn(
			refreshToken);

		// when
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/auth/logout")
				.principal(() -> String.valueOf(userDetails))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// then
		verify(tokenService, times(1)).deleteRefreshToken(anyLong(), eq(refreshToken));
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(result.getResponse().getContentAsString()).contains(SuccessMessage.LOGOUT_SUCCESS.getMessage());
	}

	@Test
	@DisplayName("닉네임 중복 체크 성공 테스트")
	public void checkNicknameSuccessTest() throws Exception {
		// given
		String nickname = faker.name().firstName();
		doNothing().when(userService).checkNicknameDuplicate(nickname);

		// when
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/check-nickname")
				.param("nickname", nickname)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// then
		verify(userService, times(1)).checkNicknameDuplicate(nickname);
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(result.getResponse().getContentAsString()).contains(SuccessMessage.NICKNAME_AVAILABLE.getMessage());
	}

	@Test
	@DisplayName("이메일 중복 체크 성공 테스트")
	public void checkEmailSuccessTest() throws Exception {
		// given
		String email = faker.internet().emailAddress();
		doNothing().when(userService).checkEmailDuplicate(email);

		// when
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/check-email")
				.param("email", email)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// then
		verify(userService, times(1)).checkEmailDuplicate(email);
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(result.getResponse().getContentAsString()).contains(SuccessMessage.NICKNAME_AVAILABLE.getMessage());
	}

	@Test
	@DisplayName("비밀번호 확인 성공 테스트")
	public void confirmPasswordSuccessTest() throws Exception {
		// given
		CustomUserDetails userDetails = new CustomUserDetails(user);
		PasswordRequest passwordRequest = new PasswordRequest("password123!");
		doNothing().when(userService).validatePassword(anyLong(), any(PasswordRequest.class));

		// when
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/confirm-password")
				.principal(() -> String.valueOf(userDetails))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(passwordRequest)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// then
		verify(userService, times(1)).validatePassword(anyLong(), any(PasswordRequest.class));
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(result.getResponse().getContentAsString()).contains(
			SuccessMessage.PASSWORD_CONFIRM_SUCCESS.getMessage());
	}

	@Test
	@DisplayName("내 프로필 조회 성공 테스트")
	public void getMyProfileSuccessTest() throws Exception {
		// given
		CustomUserDetails userDetails = new CustomUserDetails(user);
		UserProfileResponse userProfileResponse = UserProfileResponse.from(user);
		given(userService.getUserProfile(anyLong())).willReturn(userProfileResponse);

		// when
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/me")
				.principal(() -> String.valueOf(userDetails))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// then
		verify(userService, times(1)).getUserProfile(anyLong());
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(result.getResponse().getContentAsString()).contains(user.getEmail());
	}

	@Test
	@DisplayName("토큰 재발급 성공 테스트")
	public void reissueSuccessTest() throws Exception {
		// given
		String refreshToken = "refreshToken";
		String newAccessToken = "newAccessToken";
		given(tokenProvider.getCookieValue(any(HttpServletRequest.class), eq("refresh_token"))).willReturn(
			refreshToken);
		given(tokenService.reissueAccessToken(anyString())).willReturn(newAccessToken);

		// when
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/reissue")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andReturn();

		// then
		verify(tokenService, times(1)).reissueAccessToken(anyString());
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
