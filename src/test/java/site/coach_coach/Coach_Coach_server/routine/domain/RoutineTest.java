package site.coach_coach.Coach_Coach_server.routine.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoutineTest {

	@Test
	@DisplayName("코치가 루틴을 만드는 경우 테스트")
	public void testRoutineCreation() {
		// Given
		Integer userId = 1;
		Integer coachId = 2;
		Integer sportId = 3;
		String routineName = "Morning Workout";

		// When
		Routine routine = Routine.builder()
			.userId(userId)
			.coachId(coachId)
			.sportsId(sportId)
			.routineName(routineName)
			.build();

		// Then
		assertThat(routine).isNotNull();
		assertThat(routine.getId()).isNull(); // ID는 아직 생성되지 않았으므로 null이어야 함
		assertThat(routine.getUserId()).isEqualTo(userId);
		assertThat(routine.getCoachId()).isEqualTo(coachId);
		assertThat(routine.getSportsId()).isEqualTo(sportId);
		assertThat(routine.getRoutineName()).isEqualTo(routineName);
	}

	@Test
	@DisplayName("회원이 스스로 루틴을 만드는 경우 테스트")
	public void testRoutineWithNullCoachId() {
		// Given
		Integer userId = 1;
		Integer coachId = 2;
		Integer sportId = 3;
		String routineName = "Evening Workout";

		// When
		Routine routine = Routine.builder()
			.userId(userId)
			.coachId(coachId)
			.sportsId(sportId)
			.routineName(routineName)
			.build();

		// Then
		assertThat(routine).isNotNull();
		assertThat(routine.getCoachId()).isEqualTo(coachId);
	}
}
