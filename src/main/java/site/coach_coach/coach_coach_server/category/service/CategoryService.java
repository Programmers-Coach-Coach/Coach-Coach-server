package site.coach_coach.coach_coach_server.category.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.category.dto.CreateCategoryRequest;
import site.coach_coach.coach_coach_server.category.dto.UpdateCategoryInfoRequest;
import site.coach_coach.coach_coach_server.category.repository.CategoryRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;
import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final RoutineService routineService;

	@Transactional
	public Long createCategory(CreateCategoryRequest createCategoryRequest, Long routineId, Long userIdByJwt) {
		Routine routine = routineService.validateIsMyRoutine(routineId, userIdByJwt);

		Category category = Category.builder()
			.routine(routine)
			.categoryName(createCategoryRequest.categoryName())
			.isCompleted(false)
			.build();

		return categoryRepository.save(category).getCategoryId();
	}

	@Transactional
	public void deleteCategory(Long categoryId, Long userIdByJwt) {

		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CATEGORY));

		routineService.validateIsMyRoutine(category.getRoutine().getRoutineId(), userIdByJwt);

		categoryRepository.deleteById(categoryId);

	}

	@Transactional
	public void updateCategory(UpdateCategoryInfoRequest updateCategoryInfoRequest, Long categoryId, Long userIdByJwt) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CATEGORY));

		routineService.validateIsMyRoutine(category.getRoutine().getRoutineId(), userIdByJwt);

		category.updateCategoryInfo(updateCategoryInfoRequest.categoryName());
	}

	@Transactional
	@Scheduled(cron = "0 0 15 * * *")
	public void resetIsCompleted() {
		log.info("Start to reset All 'IsCompleted'.");
		categoryRepository.resetIsCompleted();
		log.info("All 'IsCompleted' has been reset.");
	}
}
