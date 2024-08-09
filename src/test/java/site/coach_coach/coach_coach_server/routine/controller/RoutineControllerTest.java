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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListResponse;
import site.coach_coach.coach_coach_server.routine.services.RoutineServices;

public class RoutineControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private RoutineServices routineServices;

	@InjectMocks
	private RoutineController routineController;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(routineController).build();
		objectMapper = new ObjectMapper();

		// Mock Authentication
		CustomUserDetails userDetails = mock(CustomUserDetails.class);
		when(userDetails.getUserId()).thenReturn(1L);
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	public void testGetRoutinesWithoutCoachId() throws Exception {
		// Arrange
		List<RoutineForListDto> mockRoutineList = List.of(new RoutineForListDto(1L, "나의헬스루틴", "헬스"));
		when(routineServices.findRoutineForListServices(any(RoutineListRequest.class))).thenReturn(mockRoutineList);

		// Act & Assert
		mockMvc.perform(get("/api/v1/routines")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(result -> {
				// Assert using AssertJ
				List<RoutineForListDto> response = objectMapper.readValue(result.getResponse().getContentAsString(),
					objectMapper.getTypeFactory().constructCollectionType(List.class, RoutineForListDto.class));
				assertThat(response).isNotNull();
				assertThat(response).hasSize(1); // 예시로 1개가 반환된다고 가정
			});
	}

	@Test
	public void testGetRoutinesWithCoachId() throws Exception {
		// Arrange
		Long coachId = 2L;
		RoutineListCoachInfoDto mockCoachInfo = new RoutineListCoachInfoDto(1L, "coach1", "coach1ImageUrl");
		List<RoutineForListDto> mockRoutineList = List.of(new RoutineForListDto(3L, "코치의헬스루틴", "헬스"));
		when(routineServices.findIsMatchingServices(any(RoutineListRequest.class))).thenReturn(true);
		when(routineServices.findRoutineListCoachInfoServices(any(RoutineListRequest.class))).thenReturn(mockCoachInfo);
		when(routineServices.findRoutineForListServices(any(RoutineListRequest.class))).thenReturn(mockRoutineList);

		// Act & Assert
		mockMvc.perform(get("/api/v1/routines?coachId=" + coachId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(result -> {
				// Assert using AssertJ
				RoutineListResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
					RoutineListResponse.class);
				assertThat(response).isNotNull();
				assertThat(response.routineListCoachInfoDto()).isEqualTo(mockCoachInfo);
				assertThat(response.routineForListDtos()).hasSize(1); // 예시로 1개가 반환된다고 가정
			});
	}
}
