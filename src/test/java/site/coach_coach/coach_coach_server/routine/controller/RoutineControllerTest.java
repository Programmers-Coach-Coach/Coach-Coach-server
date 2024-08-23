package site.coach_coach.coach_coach_server.routine.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import site.coach_coach.coach_coach_server.action.dto.ActionDto;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.category.dto.CategoryDto;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.constants.SuccessMessage;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.routine.dto.CreateRoutineRequest;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.dto.RoutineResponse;
import site.coach_coach.coach_coach_server.routine.dto.UserInfoForRoutineList;
import site.coach_coach.coach_coach_server.routine.exception.NoExistRoutineException;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@WebMvcTest(RoutineController.class)
public class RoutineControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RoutineService routineService;

	@MockBean
	private TokenProvider tokenProvider;

	@Autowired
	private RoutineController routineController;

	private RoutineListRequest routineListRequest;
	private RoutineForListDto routine;
	private List<RoutineForListDto> routineList;
	private UserInfoForRoutineList userInfoForRoutineList;
	private ObjectMapper objectMapper;
	private Long userIdByJwt;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();
		userIdByJwt = 1L;

		// Set the SecurityContext with mockUserDetails
		setSecurityContextWithMockUserDetails(userIdByJwt);

		routine = new RoutineForListDto(1L, "routineName", "sportName");
		routineList = List.of(routine);
		userInfoForRoutineList = new UserInfoForRoutineList(1L, "nickname", "profileImageUrl");
	}

	@Test
	@DisplayName("자신이 만든 루틴 목록 조회 테스트")
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

		assertThat(result.getResponse().getContentAsString()).contains(routineList.getFirst().routineName());
	}

	@Test
	@DisplayName("코치가 만들어준 루틴 목록 조회 테스트")
	public void getRoutineListByCoachTest() throws Exception {
		// Given
		Long userIdParam = 2L;
		Long coachParam = 2L;
		Long userIdByJwt = 1L;
		RoutineListRequest request = new RoutineListRequest(userIdParam, coachParam);

		// Set the SecurityContext with mockUserDetails
		setSecurityContextWithMockUserDetails(userIdByJwt);

		when(routineService.getCoachId(userIdByJwt)).thenReturn(2L);

		when(routineService.confirmIsMatching(userIdParam, coachParam, userIdByJwt)).thenReturn(request);
		when(routineService.getRoutineForList(routineListRequest)).thenReturn(routineList);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(routineList.getFirst().routineName());
	}

	@Test
	@DisplayName("자신이 만든 루틴 목록이 비어있는 경우 테스트")
	public void getRoutineListIsEmptyTest() throws Exception {
		// Given
		Long userIdByJwt = 1L;

		// Set the SecurityContext with mockUserDetails
		setSecurityContextWithMockUserDetails(userIdByJwt);

		when(routineService.confirmIsMatching(null, null, userIdByJwt)).thenReturn(routineListRequest);
		when(routineService.getRoutineForList(routineListRequest)).thenReturn(new ArrayList<>()); // Empty list

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).isEqualTo("[]");
	}

	@Test
	@DisplayName("루틴 목록 사용자 정보 조회 테스트")
	public void getUserInfoForRoutineListTest() throws Exception {
		// Given
		Long userIdByJwt = 1L;
		Long userIdParam = 2L;
		Long coachIdParam = 2L;

		setSecurityContextWithMockUserDetails(userIdByJwt);

		when(routineService.confirmIsMatching(userIdParam, coachIdParam, userIdByJwt)).thenReturn(routineListRequest);
		when(routineService.getUserInfoForRoutineList(userIdParam, coachIdParam)).thenReturn(userInfoForRoutineList);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines/user")
				.contentType(MediaType.APPLICATION_JSON)
				.queryParam("userId", userIdParam.toString())
				.queryParam("coachId", coachIdParam.toString()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(userInfoForRoutineList.nickname());
	}

	@Test
	@DisplayName("루틴 추가 성공 테스트")
	public void createRoutineSuccessTest() throws Exception {
		// Given
		Long userIdByJwt = 1L;
		Long routineId = 1L;
		CreateRoutineRequest createRoutineRequest =
			new CreateRoutineRequest(2L, 1L, "나의 헬스 루틴");

		setSecurityContextWithMockUserDetails(userIdByJwt);

		when(routineService.createRoutine(any(CreateRoutineRequest.class), any(Long.class))).thenReturn(
			routineId);

		doNothing().when(routineService).checkIsMatching(any(Long.class), any(Long.class));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/routines").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRoutineRequest)))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(
			routineId.toString());
	}

	@Test
	@DisplayName("루틴 이름 공란 작성 시 400 상태 코드 반환 및 에러 메세지")
	public void createdRoutineFailTest() throws Exception {
		// Given
		Long userIdByJwt = 1L;
		CreateRoutineRequest createRoutineRequest =
			new CreateRoutineRequest(null, 1L, "  ");

		setSecurityContextWithMockUserDetails(userIdByJwt);

		when(routineService.createRoutine(createRoutineRequest, userIdByJwt)).thenReturn(1L);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/routines").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRoutineRequest)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(ErrorMessage.INVALID_VALUE);
	}

	@Test
	@DisplayName("루틴 삭제 성공 테스트")
	public void deleteRoutineSuccessTest() throws Exception {

		doNothing().when(routineService).deleteRoutine(1L, userIdByJwt);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/routines/1").with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(
			SuccessMessage.DELETE_ROUTINE_SUCCESS.getMessage());
	}

	@Test
	@DisplayName("루틴 삭제 실패 테스트 - 미존재 루틴 삭제")
	public void deleteRoutineFailByNotExistTest() throws Exception {
		// Given
		Long routineId = 0L;
		doThrow(new NoExistRoutineException(ErrorMessage.NOT_FOUND_ROUTINE)).when(routineService)
			.deleteRoutine(anyLong(), anyLong());

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/routines/" + routineId).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(
			ErrorMessage.NOT_FOUND_ROUTINE);
	}

	@Test
	@DisplayName("루틴 삭제 실패 테스트 - 소유하지 않은 루틴 접근")
	public void deleteRoutineFailTest() throws Exception {
		// Given
		Long routineId = 0L;

		doThrow(new AccessDeniedException()).when(routineService)
			.deleteRoutine(routineId, userIdByJwt);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/routines/" + routineId).with(csrf()))
			.andExpect(MockMvcResultMatchers.status().isForbidden())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(
			ErrorMessage.ACCESS_DENIED);
	}

	@Test
	@DisplayName("루틴 개별 조회 성공 - 일반 회원")
	public void getRoutineSuccessTest() throws Exception {
		// Given
		Long userIdParam = 1L;
		ActionDto actionDto = new ActionDto(1L, "actionName", 5, "10", "description");
		List<ActionDto> actionList = new ArrayList<>();
		actionList.add(actionDto);
		CategoryDto categoryDto = new CategoryDto(1L, "categoryName", false, actionList);
		List<CategoryDto> categoryList = new ArrayList<>();
		categoryList.add(categoryDto);
		RoutineResponse routineResponse = new RoutineResponse("routineName", categoryList);

		when(routineService.getRoutineDetail(anyLong(), anyLong(), anyLong())).thenReturn(
			routineResponse);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routines/" + routine.routineId())
				.contentType(MediaType.APPLICATION_JSON)
				.queryParam(("userId"), userIdParam.toString()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains(actionDto.actionName());
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
