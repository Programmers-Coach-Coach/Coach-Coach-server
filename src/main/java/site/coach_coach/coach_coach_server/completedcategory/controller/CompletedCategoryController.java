package site.coach_coach.coach_coach_server.completedcategory.controller;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.category.service.CategoryService;
import site.coach_coach.coach_coach_server.completedcategory.dto.CompletedCategoryResponse;
import site.coach_coach.coach_coach_server.completedcategory.service.CompletedCategoryService;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CompletedCategoryController {
	private final CompletedCategoryService completedCategoryService;
	private final RoutineService routineService;
	private final CategoryService categoryService;

	@PostMapping("/v1/routines/{routineId}/{categoryId}/completed")
	public ResponseEntity<CompletedCategoryResponse> manageCategoryCompletion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "routineId") Long routineId,
		@PathVariable(name = "categoryId") Long categoryId
	) {

		Long userIdByJwt = userDetails.getUserId();
		// 루틴 존재 확인
		routineService.validateBeforeCompleteCategory(routineId, userIdByJwt);
		// 카테고리 존재 확인 및 is_completed 변경
		Category category = categoryService.changeIsCompleted(categoryId);

		if (category.getIsCompleted()) {
			// 카테고리 완료로 변 : completedCategory에 row 추가
			Date now = java.sql.Date.valueOf(LocalDate.now());
			System.out.println(now);
			completedCategoryService.createCompletedCategory(userIdByJwt, category, now);
			return ResponseEntity.status(HttpStatus.CREATED).body(new CompletedCategoryResponse(categoryId));
		}
		// 카테고리 미완료로 변경 : comtedCategory에서 row 삭제
		return ResponseEntity.ok(new CompletedCategoryResponse(categoryId));
	}

}
