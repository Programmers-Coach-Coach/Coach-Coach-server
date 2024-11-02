package site.coach_coach.coach_coach_server.action.service;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.action.domain.Action;
import site.coach_coach.coach_coach_server.action.dto.ActionDto;
import site.coach_coach.coach_coach_server.action.repository.ActionRepository;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

@Service
@RequiredArgsConstructor
public class ActionService {
	private final ActionRepository actionRepository;
	private final JdbcTemplate jdbcTemplate;
	private int indexOfAction = 0;

	@Transactional
	public void createAction(Routine newRoutine, List<ActionDto> actions) {

		int newActionSize = actions.size();

		String sql = "INSERT INTO actions (routine_id, action_name, sets, counts_or_minutes) VALUES (?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql,
			actions,
			actions.size(),
			(PreparedStatement ps, ActionDto action) -> {
				ps.setLong(1, newRoutine.getRoutineId());
				ps.setString(2, action.actionName());
				ps.setInt(3, action.sets());
				ps.setInt(4, action.countsOrMinutes());

			});

		if (newActionSize < 4) {
			for (int j = 0; j < 4 - newActionSize; j++) {
				Action emptyAction = Action.builder()
					.routine(newRoutine)
					.actionName(null)
					.sets(null)
					.countsOrMinutes(null).build();
				actionRepository.save(emptyAction);
			}
		}
	}

	@Transactional
	public void updateAction(Long routineId, List<ActionDto> newActions) {
		List<Action> actions = actionRepository.findByRoutine_RoutineId(routineId);

		newActions.forEach((newAction) -> {
			actions.get(indexOfAction).updateActionInfo(newAction);
			indexOfAction++;
		});

		while (indexOfAction < 4) {
			actions.get(indexOfAction).resetActionInfo();
			indexOfAction++;
		}
		indexOfAction = 0;
	}
}
