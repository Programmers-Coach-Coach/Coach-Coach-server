package site.coach_coach.coach_coach_server.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import site.coach_coach.coach_coach_server.category.service.CategoryService;

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
}
