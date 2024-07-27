package site.coach_coach.Coach_Coach_server.routine.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import site.coach_coach.Coach_Coach_server.routine.dto.RoutineDTO;
import site.coach_coach.Coach_Coach_server.routine.domain.Routine;
import site.coach_coach.Coach_Coach_server.routine.repository.RoutineRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RoutineServicesTest {

    @Mock
    private RoutineRepository routineRepository;

    @InjectMocks
    private RoutineServices routineServices;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("루틴이 DB에 추가되고 Spring JPA의 save 메소드 결과 반환되는지 테스트")
    public void testAddRoutine() {
        // Given
        RoutineDTO routineDto = RoutineDTO.builder()
                .userId(1)
                .coachId(2)
                .sportId(3)
                .routineName("Test Routine")
                .build();

        Routine expectedRoutine = Routine.builder()
                .userId(1)
                .coachId(2)
                .sportId(3)
                .routineName("Test Routine")
                .build();

        // Mocking the repository behavior
        when(routineRepository.save(any(Routine.class))).thenReturn(expectedRoutine);

        // When
        Routine actualRoutine = routineServices.addRoutine(routineDto);

        // Then
        assertEquals(expectedRoutine, actualRoutine);
    }
}
