package site.coach_coach.coach_coach_server.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.category.dto.CreateCategoryRequest;
import site.coach_coach.coach_coach_server.category.exception.NotFoundCategoryException;
import site.coach_coach.coach_coach_server.category.repository.CategoryRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.completedcategory.exception.DuplicateCompletedCategoryException;
import site.coach_coach.coach_coach_server.completedcategory.exception.NotFoundCompletedCategoryException;
import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final RoutineService routineService;

	@Transactional
	public Long createCategory(CreateCategoryRequest createCategoryRequest, Long routineId, Long userIdByJwt) {
		Routine routine = routineService.validateAccessToRoutine(routineId, userIdByJwt);

		Category category = Category.builder()
			.routine(routine)
			.categoryName(createCategoryRequest.categoryName())
			.isCompleted(false)
			.build();

		return categoryRepository.save(category).getCategoryId();
	}

	@Transactional
	public void deleteCategory(Long routineId, Long categoryId, Long userIdByJwt) {
		routineService.validateAccessToRoutine(routineId, userIdByJwt);

		Boolean isExistCategory = categoryRepository.existsById(categoryId);
		if (isExistCategory) {
			categoryRepository.deleteById(categoryId);
		} else {
			throw new NotFoundCategoryException(ErrorMessage.NOT_FOUND_CATEGORY);
		}
	}

	@Transactional
	public Category changeIsCompleted(Long categoryId, Long routineId, Boolean inputIsCompleted) {
		Category category = categoryRepository.findByCategoryIdAndRoutine_RoutineId(categoryId, routineId)
			.orElseThrow(() -> new NotFoundCategoryException(ErrorMessage.NOT_FOUND_CATEGORY));

		if (category.getIsCompleted().equals(inputIsCompleted)) {
			if (inputIsCompleted) {
				throw new DuplicateCompletedCategoryException(ErrorMessage.DUPLICATE_COMPLETED_CATEGORY);
			} else {
				throw new NotFoundCompletedCategoryException(ErrorMessage.NOT_FOUND_COMPLETED_CATEGORY);
			}
		}

		// Dirty Checking
		category.setIsCompleted(!category.getIsCompleted());
		return category;
	}
}
