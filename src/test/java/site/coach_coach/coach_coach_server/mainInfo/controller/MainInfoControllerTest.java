package site.coach_coach.coach_coach_server.mainInfo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.*;
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

import site.coach_coach.coach_coach_server.auth.jwt.TokenFilter;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.maininfo.controller.*;
import site.coach_coach.coach_coach_server.maininfo.dto.MainResponseDto;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;
import site.coach_coach.coach_coach_server.coach.dto.CoachDto;
import site.coach_coach.coach_coach_server.maininfo.service.MainService;

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

		CoachDto coachDto = CoachDto.builder()
			.coachId(1L)
			.coachName("John Doe")
			.profileImageUrl("http://example.com/image.jpg")
			.description("Experienced coach in multiple sports.")
			.countOfLikes(10)
			.liked(true)
			.coachingSports(new ArrayList<>())
			.build();

		SportDto sportDto = SportDto.builder()
			.sportId(1L)
			.sportName("Soccer")
			.sportImageUrl("http://example.com/soccer.jpg")
			.build();

		List<CoachDto> coaches = List.of(coachDto);
		List<SportDto> sports = List.of(sportDto);

		mainResponseDto = MainResponseDto.builder()
			.coaches(coaches)
			.sports(sports)
			.build();
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

		// 서비스가 반환할 값을 설정
		when(mainService.getMainResponse(mockUserDetails.getUser())).thenReturn(mainResponseDto);

		// API 호출 및 응답 검증
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/main")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		// 응답 내용 검증
		assertThat(result.getResponse().getContentAsString()).contains("John Doe");
		assertThat(result.getResponse().getContentAsString()).contains("Soccer");
	}
}
