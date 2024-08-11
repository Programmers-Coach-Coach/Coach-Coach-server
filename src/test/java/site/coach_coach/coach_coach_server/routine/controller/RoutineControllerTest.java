package site.coach_coach.coach_coach_server.routine.controller;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import site.coach_coach.coach_coach_server.auth.jwt.TokenFilter;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.services.RoutineService;

@WebMvcTest(RoutineController.class)
public class RoutineControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RoutineService routineService;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private TokenFilter tokenFilter;

	@InjectMocks
	private RoutineController routineController;

	private RoutineListRequest routineListRequest_Myself;
	private RoutineListRequest routineListRequest_Coach;
	private RoutineListRequest routineListRequest_NoMatch;

	private RoutineForListDto routine;

	private RoutineListCoachInfoDto routineListCoachInfo;

	private List<RoutineForListDto> routineList;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		routine = new RoutineForListDto(1L, "routineName", "sportName");
		routineList = List.of(routine);
		routineListCoachInfo = new RoutineListCoachInfoDto(1L, "coachName", "coachProfileUrl");
	}

	@Test
	@WithMockUser
	public void getRoutineList_WithCoachIdTest() throws Exception {
		// Given
		Long userId = 1L;
		routineListRequest_Myself = new RoutineListRequest(1L, null);

		// Mock CustomUserDetails
		CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
		when(mockUserDetails.getUserId()).thenReturn(userId);

		// Set the SecurityContext with mockUserDetails
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(
			new UsernamePasswordAuthenticationToken(mockUserDetails, null, new ArrayList<>())
		);
		SecurityContextHolder.setContext(securityContext);

		when(routineService.getRoutineForList(routineListRequest_Coach)).thenReturn(routineList);
		when(routineService.getRoutineListCoachInfo(routineListRequest_Coach)).thenReturn(routineListCoachInfo);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString().contains(routineListCoachInfo.coachId().toString()));
	}

	@Test
	public void getRoutineList_WithoutCoachIdTest() throws Exception {
		// Given
		Long userId = 1L;
		Long coachId = null;
		routineListRequest_Coach = new RoutineListRequest(1L, 1L);

		// Mock CustomUserDetails
		CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
		when(mockUserDetails.getUserId()).thenReturn(userId);

		// Set the SecurityContext with mockUserDetails
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(
			new UsernamePasswordAuthenticationToken(mockUserDetails, null, new ArrayList<>())
		);
		SecurityContextHolder.setContext(securityContext);

		when(routineService.getRoutineForList(routineListRequest_Myself)).thenReturn(routineList);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString().contains(routine.routineName()));
	}

	@Test
	public void getRoutineList_NoMatchingCoachTest() throws Exception {
		Long userId = 1L;
		Long coachId = 2L;
		routineListRequest_NoMatch = new RoutineListRequest(userId, coachId);
		// Mock CustomUserDetails
		CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
		when(mockUserDetails.getUserId()).thenReturn(userId);

		// Set the SecurityContext with mockUserDetails
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(
			new UsernamePasswordAuthenticationToken(mockUserDetails, null, new ArrayList<>())
		);
		SecurityContextHolder.setContext(securityContext);

		when(routineService.getIsMatching(routineListRequest_NoMatch)).thenReturn(false);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines")
				.queryParam("coachId", String.valueOf(coachId)))
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andReturn();

		assertThat(result.getResponse().getContentAsString().contains("매칭되지 않은 코치입니다."));
	}
}
