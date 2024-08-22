package site.coach_coach.coach_coach_server.category.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.category.dto.CreateCategoryRequest;
import site.coach_coach.coach_coach_server.category.repository.CategoryRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.exception.NoExistRoutineException;
import site.coach_coach.coach_coach_server.routine.repository.RoutineRepository;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final RoutineService routineService;
	private final RoutineRepository routineRepository;

	public Long createCategory(CreateCategoryRequest createCategoryRequest, Long routineId, Long userIdByJwt) {
		Routine routine = validateAccessToRoutine(routineId, userIdByJwt);

		Category category = Category.builder()
			.routine(routine)
			.categoryName(createCategoryRequest.categoryName())
			.isCompleted(false)
			.build();

		return categoryRepository.save(category).getCategoryId();
	}

	public Routine validateAccessToRoutine(Long routineId, Long userIdByJwt) {
		Routine routine = routineRepository.findById(routineId)
			.orElseThrow(() -> new NoExistRoutineException(ErrorMessage.NOT_FOUND_ROUTINE));

		if (!routine.getUserId().equals(userIdByJwt)) {
			Long coachId = routineService.getCoachId(userIdByJwt);
			if (routine.getCoachId() != coachId) {
				throw new AccessDeniedException();
			}
		} else if (routine.getCoachId() != null) {
			throw new AccessDeniedException();
		}

		return routine;
	}
}
