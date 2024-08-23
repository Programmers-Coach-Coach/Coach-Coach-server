package site.coach_coach.coach_coach_server.completedcategory.controller;

import java.time.LocalDate;

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
import site.coach_coach.coach_coach_server.common.response.SuccessIdResponse;
import site.coach_coach.coach_coach_server.completedcategory.service.CompletedCategoryService;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;
import site.coach_coach.coach_coach_server.userrecord.service.UserRecordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CompletedCategoryController {
	private final CompletedCategoryService completedCategoryService;
	private final RoutineService routineService;
	private final CategoryService categoryService;
	private final UserRecordService userRecordService;

	@PostMapping("/v1/routines/{routineId}/{categoryId}/completed")
	public ResponseEntity<SuccessIdResponse> manageCategoryCompletion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "routineId") Long routineId,
		@PathVariable(name = "categoryId") Long categoryId
	) {
		Long userIdByJwt = userDetails.getUserId();

		routineService.validateBeforeCompleteCategory(routineId, userIdByJwt);
		Category category = categoryService.changeIsCompleted(categoryId);
		LocalDate localDate = LocalDate.now();
		UserRecord userRecord = userRecordService.getUserRecordForCompleteCategory(userIdByJwt, localDate);

		if (category.getIsCompleted()) {
			Long completedCategoryId = completedCategoryService.createCompletedCategory(userRecord, category);
			return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessIdResponse(completedCategoryId));
		} else {
			completedCategoryService.deleteCompletedCategory(userRecord, category);
			return ResponseEntity.noContent().build();
		}
	}

}
