package site.coach_coach.coach_coach_server.completedcategory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.response.SuccessIdResponse;
import site.coach_coach.coach_coach_server.completedcategory.service.CompletedCategoryService;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CompletedCategoryController {
	private final CompletedCategoryService completedCategoryService;
	private final RoutineService routineService;

	@PostMapping("/v1/categories/{categoryId}/completed")
	public ResponseEntity<SuccessIdResponse> createCategoryCompletion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "categoryId") Long categoryId
	) {
		Long userIdByJwt = userDetails.getUserId();
		Long completedCategoryId = completedCategoryService.createCompletedCategory(categoryId, userIdByJwt);
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessIdResponse(completedCategoryId));

	}

	@DeleteMapping("/v1/categories/{categoryId}/completed")
	public ResponseEntity<Void> deleteCategoryCompletion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "categoryId") Long categoryId
	) {
		Long userIdByJwt = userDetails.getUserId();
		completedCategoryService.deleteCompletedCategory(categoryId, userIdByJwt);
		return ResponseEntity.noContent().build();
	}

}
