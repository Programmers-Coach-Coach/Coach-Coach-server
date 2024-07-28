package site.coach_coach.Coach_Coach_server.routine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import site.coach_coach.Coach_Coach_server.routine.dto.RoutineDTO;
import site.coach_coach.Coach_Coach_server.routine.domain.Routine;
import site.coach_coach.Coach_Coach_server.routine.services.RoutineServices;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
    }

    @Test
    @DisplayName("루틴이 DB에 추가가 잘 되었는지 테스트")
    public void testAddRoutine_Success() throws Exception {
        // Given
        Integer userId = 1;
        Integer coachId = 2;
        Integer sportId = 3;
        String routineName = "Test Routine";

        RoutineDTO routineDto = new RoutineDTO(userId, coachId, sportId, routineName);
        Routine routine = new Routine();
        routine.setId(1); // Assume the routine is created successfully with an ID

        when(routineServices.addRoutineService(any(RoutineDTO.class))).thenReturn(routine);

        // When & Then
        mockMvc.perform(post("/routines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routineDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Add Routine Success"));
    }

    @Test
    @DisplayName("루틴이 DB에 추가되지 않는 경우 테스트")
    public void testAddRoutine_Failure() throws Exception {
        // Given
        Integer userId = 1;
        Integer coachId = 2;
        Integer sportId = 3;
        String routineName = "Test Routine";

        RoutineDTO routineDto = new RoutineDTO(userId, coachId, sportId, routineName);
        Routine routine = new Routine();
        routine.setId(null); // Assume the routine creation failed

        when(routineServices.addRoutineService(any(RoutineDTO.class))).thenReturn(routine);

        // When & Then
        mockMvc.perform(post("/routines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routineDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Add Routine Fail"));
    }
}
