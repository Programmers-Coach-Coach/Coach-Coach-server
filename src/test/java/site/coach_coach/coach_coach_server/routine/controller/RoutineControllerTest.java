package site.coach_coach.coach_coach_server.routine.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import site.coach_coach.coach_coach_server.auth.jwt.TokenFilter;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListResponse;
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

	private RoutineForListDto routine;

	private RoutineListCoachInfoDto routineListCoachInfo;

	private List<RoutineForListDto> routineForListDto;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		routine = new RoutineForListDto(1L, "routineName", "sportName");
		routineForListDto = List.of(routine);
		routineListCoachInfo = new RoutineListCoachInfoDto(2L, "coachName", "coachProfileUrl");
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void getRoutineList_WithCoachIdTest() throws Exception {
		// Given
		Long userId = 1L;
		Long coachId = 2L;
		RoutineListRequest request = new RoutineListRequest(userId, coachId);

		RoutineListResponse response = new RoutineListResponse(routineListCoachInfo, routineForListDto);

		when(routineService.getIsMatching(any())).thenReturn(true);
		when(routineService.getRoutineListCoachInfo(any())).thenReturn(routineListCoachInfo);
		when(routineService.getRoutineForList(any())).thenReturn(routineForListDto);

		// When & Then
		MvcResult result = mockMvc.perform(get("/api/v1/routines")
				.param("coachId", String.valueOf(coachId))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		// AssertJ validation
		String jsonResponse = result.getResponse().getContentAsString();
		assertThat(jsonResponse).contains("\"coachId\":2", "\"coachName\":\"coachName\"",
			"\"routineName\":\"routineName\"");

		verify(routineService, times(1)).getIsMatching(any());
		verify(routineService, times(1)).getRoutineListCoachInfo(any());
		verify(routineService, times(1)).getRoutineForList(any());
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void getRoutineList_WithoutCoachIdTest() throws Exception {
		// Given
		Long userId = 1L;
		RoutineListRequest request = new RoutineListRequest(userId, null);

		RoutineListResponse response = new RoutineListResponse(null, routineForListDto);

		when(routineService.getIsMatching(any())).thenReturn(true);
		when(routineService.getRoutineForList(any())).thenReturn(routineForListDto);

		// When & Then
		MvcResult result = mockMvc.perform(get("/api/v1/routines")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		// AssertJ validation
		String jsonResponse = result.getResponse().getContentAsString();
		assertThat(jsonResponse).contains("\"routineName\":\"routineName\"", "\"sportName\":\"sportName\"");

		verify(routineService, times(1)).getIsMatching(any());
		verify(routineService, times(1)).getRoutineForList(any());
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void getRoutineList_NoMatchingCoachTest() throws Exception {
		// Given
		Long userId = 1L;
		Long coachId = 2L;

		when(routineService.getIsMatching(any())).thenReturn(false);

		// When & Then
		MvcResult result = mockMvc.perform(get("/api/v1/routines")
				.param("coachId", String.valueOf(coachId))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(content().string("message : 매칭되지 않은 코치입니다."))
			.andReturn();

		// AssertJ validation
		String jsonResponse = result.getResponse().getContentAsString();
		assertThat(jsonResponse).contains("매칭되지 않은 코치입니다.");

		verify(routineService, times(1)).getIsMatching(any());
	}
}
