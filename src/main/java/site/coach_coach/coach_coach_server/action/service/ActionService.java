package site.coach_coach.coach_coach_server.action.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.action.domain.Action;
import site.coach_coach.coach_coach_server.action.dto.CreateActionRequest;
import site.coach_coach.coach_coach_server.action.dto.UpdateActionInfoRequest;
import site.coach_coach.coach_coach_server.action.repository.ActionRepository;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.category.repository.CategoryRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@Service
@RequiredArgsConstructor
public class ActionService {
	private final RoutineService routineService;
	private final CategoryRepository categoryRepository;
	private final ActionRepository actionRepository;

	@Transactional
	public Long createAction(Long categoryId, Long userIdByJwt,
		CreateActionRequest createActionRequest) {
		Category category = categoryRepository.checkIsExistCategory(categoryId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CATEGORY));

		routineService.validateIsMyRoutine(category.getRoutine().getRoutineId(), userIdByJwt);

		Action action = Action.of(createActionRequest, category);
		return actionRepository.save(action).getActionId();
	}

	@Transactional
	public void deleteAction(Long actionId, Long userIdByJwt) {
		Action action = actionRepository.checkIsExistAction(actionId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_ACTION));

		routineService.validateIsMyRoutine(action.getCategory().getRoutine().getRoutineId(), userIdByJwt);

		actionRepository.deleteById(actionId);
	}

	@Transactional
	public void updateActionInfo(UpdateActionInfoRequest updateActionInfoRequest, Long actionId, Long userIdByJwt) {
		Action action = actionRepository.checkIsExistAction(actionId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_ACTION));

		routineService.validateIsMyRoutine(action.getCategory().getRoutine().getRoutineId(), userIdByJwt);
		action.updateActionInfo(updateActionInfoRequest);
	}
}
