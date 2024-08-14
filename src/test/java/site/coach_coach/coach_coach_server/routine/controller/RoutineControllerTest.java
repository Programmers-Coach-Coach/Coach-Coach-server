package site.coach_coach.coach_coach_server.routine.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.dto.UserInfoForRoutineList;
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

	private RoutineListRequest routineListRequest;
	private RoutineForListDto routine;
	private List<RoutineForListDto> routineList;
	private UserInfoForRoutineList userInfoForRoutineList;

	@BeforeEach
	public void setUp() {
		routine = new RoutineForListDto(1L, "routineName", "sportName");
		routineList = List.of(routine);
		userInfoForRoutineList = new UserInfoForRoutineList(1L, "nickname", "profileImageUrl");

	}

	@Test
	public void getRoutineListByMyselfTest() throws Exception {
		// Given
		Long userIdParam = null;
		Long coachIdParam = null;
		Long userIdByJwt = 1L;

		// Set the SecurityContext with mockUserDetails
		setSecurityContextWithMockUserDetails(userIdByJwt);

		when(routineService.confirmIsMatching(userIdParam, coachIdParam, userIdByJwt)).thenReturn(routineListRequest);
		when(routineService.getRoutineForList(routineListRequest)).thenReturn(routineList);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString().contains(routineList.getFirst().routineName()));
	}

	@Test
	public void getRoutineListByCoachTest() throws Exception {
		// Given
		Long userIdParam = 2L;
		Long coachParam = 2L;
		Long userIdByJwt = 1L;

		// Set the SecurityContext with mockUserDetails
		setSecurityContextWithMockUserDetails(userIdByJwt);

		when(routineService.getCoachId(userIdByJwt)).thenReturn(2L);

		when(routineService.confirmIsMatching(userIdParam, coachParam, userIdByJwt)).thenReturn(routineListRequest);
		when(routineService.getRoutineForList(routineListRequest)).thenReturn(routineList);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString().contains(routineList.getFirst().routineName()));
	}

	@Test
	public void getUserInfoForRoutineListTest() throws Exception {
		// Given
		Long userIdByJwt = 1L;
		Long userIdParam = 2L;
		Long coachIdParam = 2L;

		setSecurityContextWithMockUserDetails(userIdByJwt);

		when(routineService.confirmIsMatching(userIdParam, coachIdParam, userIdByJwt)).thenReturn(routineListRequest);
		when(routineService.getUserInfoForRoutineList(userIdParam, coachIdParam)).thenReturn(userInfoForRoutineList);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines/user"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString().contains(userInfoForRoutineList.nickname()));

	}

	public void setSecurityContextWithMockUserDetails(Long userIdByJwt) {
		// Mock CustomUserDetails
		CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
		when(mockUserDetails.getUserId()).thenReturn(userIdByJwt);

		// Set the SecurityContext with mockUserDetails
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(
			new UsernamePasswordAuthenticationToken(mockUserDetails, null, new ArrayList<>())
		);
		SecurityContextHolder.setContext(securityContext);
	}

}
