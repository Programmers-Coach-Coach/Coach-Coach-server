package site.coach_coach.coach_coach_server.routine.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.services.RoutineServices;

@WebMvcTest(RoutineController.class)
public class RoutineControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private RoutineServices routineServices;

	@InjectMocks
	private RoutineController routineController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetRoutinesWithValidCoachId() throws Exception {
		Long userId = 2L;
		Long coachId = 1L;
		Integer isMatching = 1;

		// Mocking the service responses
		when(routineServices.searchIsMatching(userId, Optional.of(coachId))).thenReturn(Optional.of(isMatching));

		RoutineForListDto routineForListDto = Mockito.mock(RoutineForListDto.class);
		RoutineListCoachInfoDto routineListCoachInfoDto = Mockito.mock(RoutineListCoachInfoDto.class);

		when(routineServices.searchRoutines(userId, Optional.of(coachId))).thenReturn(
			Collections.singletonList(routineForListDto));
		when(routineServices.searchRoutineCoachInfo(Optional.of(coachId))).thenReturn(
			Optional.of(routineListCoachInfoDto));

		// Perform the GET request
		String response = mockMvc.perform(get("/api/v1/routines")
				.param("coachId", String.valueOf(coachId))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();

		// Assert the response
		assertThat(response).contains("routineListCoachInfoDto");
		assertThat(response).contains("routineForListDto");
	}

	@Test
	public void testGetRoutinesWithInvalidCoachId() throws Exception {
		Long userId = 2L;
		Long coachId = 1L;
		Integer isMatching = 0;
		// Mocking the service response for invalid coachId
		when(routineServices.searchIsMatching(userId, Optional.of(coachId))).thenReturn(Optional.of(isMatching));

		// Perform the GET request
		String response = mockMvc.perform(get("/api/v1/routines")
				.param("coachId", String.valueOf(coachId))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse().getContentAsString();

		// Assert the response
		assertThat(response).isEqualTo("잘못된 coachId를 입력하셨습니다.");
	}

	@Test
	public void testGetRoutinesWithoutCoachId() throws Exception {
		Long userId = 2L;
		Integer isMatching = 1;
		// Mocking the service response
		when(routineServices.searchIsMatching(userId, Optional.empty())).thenReturn(Optional.of(isMatching));

		RoutineForListDto routineForListDto = Mockito.mock(RoutineForListDto.class);
		when(routineServices.searchRoutines(userId, Optional.empty())).thenReturn(
			Collections.singletonList(routineForListDto));

		// Perform the GET request
		String response = mockMvc.perform(get("/api/v1/routines")
				.param("coachId", "")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();

		// Assert the response
		assertThat(response).contains("routineForListDto");
		assertThat(response).doesNotContain("routineListCoachInfoDto");
	}

}
