package site.coach_coach.coach_coach_server.mainInfo.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.maininfo.controller.MainInfoController;
import site.coach_coach.coach_coach_server.maininfo.dto.MainInfoCoachDto;
import site.coach_coach.coach_coach_server.maininfo.dto.MainInfoResponseDto;
import site.coach_coach.coach_coach_server.maininfo.service.MainInfoService;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;
import site.coach_coach.coach_coach_server.user.domain.User;

@WebMvcTest(MainInfoController.class)
public class MainInfoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MainInfoService mainInfoService;

	@MockBean
	private TokenProvider tokenProvider;

	@InjectMocks
	private MainInfoController mainInfoController;

	private MainInfoResponseDto mainInfoResponseDto;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		MainInfoCoachDto mainInfoCoachDto = new MainInfoCoachDto(
			1L,
			"John Doe",
			"http://example.com/image.jpg",
			"Experienced coach in multiple sports.",
			10,
			true,
			new ArrayList<>()
		);

		SportDto sportDto = new SportDto(
			1L,
			"health",
			"http://example.com/health.jpg"
		);

		List<MainInfoCoachDto> coaches = List.of(mainInfoCoachDto);
		List<SportDto> sports = List.of(sportDto);

		mainInfoResponseDto = new MainInfoResponseDto(sports, coaches);
	}

	@Test
	public void getMainInfoTest() throws Exception {
		// Given
		Long userId = 1L;
		User testUser = User.builder()
			.userId(userId)
			.nickname("John Doe")
			.build();

		// Mock CustomUserDetails
		CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
		when(mockUserDetails.getUser()).thenReturn(testUser);

		// Set the SecurityContext with mockUserDetails
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(
			new UsernamePasswordAuthenticationToken(mockUserDetails, null, new ArrayList<>())
		);
		SecurityContextHolder.setContext(securityContext);

		when(mainInfoService.getMainInfoResponse(mockUserDetails.getUser())).thenReturn(mainInfoResponseDto);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/main-info")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains("John Doe");
		assertThat(result.getResponse().getContentAsString()).contains("health");
	}
}
