package site.coach_coach.coach_coach_server.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.category.dto.CreateCategoryRequest;
import site.coach_coach.coach_coach_server.category.dto.CreateCategoryResponse;
import site.coach_coach.coach_coach_server.category.dto.UpdateCategoryInfoRequest;
import site.coach_coach.coach_coach_server.category.service.CategoryService;
import site.coach_coach.coach_coach_server.common.constants.SuccessMessage;
import site.coach_coach.coach_coach_server.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
	private final CategoryService categoryService;

	@PostMapping("/v1/routines/{routineId}")
	public ResponseEntity<CreateCategoryResponse> createCategory(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "routineId") Long routineId,
		@RequestBody @Valid CreateCategoryRequest createCategoryRequest
	) {
		Long userIdByJWt = userDetails.getUserId();
		Long newCategoryId = categoryService.createCategory(createCategoryRequest, routineId, userIdByJWt);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new CreateCategoryResponse(HttpStatus.CREATED.value(), newCategoryId));
	}

	@DeleteMapping("/v1/categories{categoryId}")
	public ResponseEntity<SuccessResponse> deleteCategory(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "categoryId") Long categoryId
	) {
		Long userIdByJwt = userDetails.getUserId();
		categoryService.deleteCategory(categoryId, userIdByJwt);
		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.DELETE_CATEGORY_SUCCESS.getMessage()));
	}

	@PatchMapping("/v1/categories/{categoryId}")
	public ResponseEntity<SuccessResponse> updateCategory(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "categoryId") Long categoryId,
		@RequestBody @Valid UpdateCategoryInfoRequest updateCategoryInfoRequest
	) {
		Long userIdByJwt = userDetails.getUserId();
		categoryService.updateCategory(updateCategoryInfoRequest, categoryId, userIdByJwt);

		return ResponseEntity.noContent().build();
	}
}
