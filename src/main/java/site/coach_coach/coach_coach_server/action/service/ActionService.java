package site.coach_coach.coach_coach_server.action.service;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.action.domain.Action;
import site.coach_coach.coach_coach_server.action.dto.UpdateActionInfoRequest;
import site.coach_coach.coach_coach_server.action.repository.ActionRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@Service
@RequiredArgsConstructor
public class ActionService {
	private final RoutineService routineService;
	private final ActionRepository actionRepository;
	private final JdbcTemplate jdbcTemplate;

	@Transactional
	public void createAction(Long newRoutineId, List<Action> actions) {

		String sql = "INSERT INTO actions (routine_id, action_name, sets, counts_or_minutes) VALUES (?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql,
			actions,
			actions.size(),
			(PreparedStatement ps, Action action) -> {
				ps.setLong(1, newRoutineId);
				ps.setString(2, action.getActionName());
				ps.setInt(3, action.getSets());
				ps.setInt(4, action.getCountsOrMinutes());
			});
	}

	@Transactional
	public void deleteAction(Long actionId, Long userIdByJwt) {
		Action action = actionRepository.findExistAction(actionId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_ACTION));

		routineService.validateIsMyRoutine(action.getRoutine().getRoutineId(), userIdByJwt);

		actionRepository.deleteById(actionId);
	}

	@Transactional
	public void updateActionInfo(UpdateActionInfoRequest updateActionInfoRequest, Long actionId, Long userIdByJwt) {
		Action action = actionRepository.findExistAction(actionId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_ACTION));

		routineService.validateIsMyRoutine(action.getRoutine().getRoutineId(), userIdByJwt);
		action.updateActionInfo(updateActionInfoRequest);
	}
}
