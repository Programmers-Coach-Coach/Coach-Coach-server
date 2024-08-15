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
import site.coach_coach.coach_coach_server.coach.dto.CoachDto;
import site.coach_coach.coach_coach_server.maininfo.controller.MainInfoController;
import site.coach_coach.coach_coach_server.maininfo.dto.MainResponseDto;
import site.coach_coach.coach_coach_server.maininfo.service.MainService;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;

@WebMvcTest(MainInfoController.class)
public class MainInfoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MainService mainService;

	@MockBean
	private TokenProvider tokenProvider;

	@InjectMocks
	private MainInfoController mainInfoController;

	private MainResponseDto mainResponseDto;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		CoachDto coachDto = new CoachDto(
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
			"http://example.com/soccer.jpg"
		);

		List<CoachDto> coaches = List.of(coachDto);
		List<SportDto> sports = List.of(sportDto);

		mainResponseDto = new MainResponseDto(sports, coaches);
	}

	@Test
	public void getMainInfoTest() throws Exception {
		// Given
		Long userId = 1L;

		// Mock CustomUserDetails
		CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
		when(mockUserDetails.getUserId()).thenReturn(userId);

		// Set the SecurityContext with mockUserDetails
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(
			new UsernamePasswordAuthenticationToken(mockUserDetails, null, new ArrayList<>())
		);
		SecurityContextHolder.setContext(securityContext);

		when(mainService.getMainResponse(mockUserDetails.getUser())).thenReturn(mainResponseDto);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/main")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains("John Doe");
		assertThat(result.getResponse().getContentAsString()).contains("health");
	}
}
