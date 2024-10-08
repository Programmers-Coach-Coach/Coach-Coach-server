package site.coach_coach.coach_coach_server.action.controller;

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
import site.coach_coach.coach_coach_server.action.dto.CreateActionRequest;
import site.coach_coach.coach_coach_server.action.dto.UpdateActionInfoRequest;
import site.coach_coach.coach_coach_server.action.service.ActionService;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.response.SuccessIdResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActionController {
	private final ActionService actionService;

	@PostMapping("/v1/categories/{categoryId}")
	public ResponseEntity<SuccessIdResponse> createActionIntoCategory(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "categoryId") Long categoryId,
		@RequestBody @Valid CreateActionRequest createActionRequest
	) {
		Long userIdByJwt = userDetails.getUserId();
		Long actionId = actionService.createAction(categoryId, userIdByJwt, createActionRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessIdResponse(actionId));
	}

	@DeleteMapping("/v1/actions/{actionId}")
	public ResponseEntity<Void> deleteActionInCategory(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "actionId") Long actionId
	) {
		Long userIdByJwt = userDetails.getUserId();
		actionService.deleteAction(actionId, userIdByJwt);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/v1/actions/{actionId}")
	public ResponseEntity<Void> updateActionInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "actionId") Long actionId,
		@RequestBody @Valid UpdateActionInfoRequest updateActionInfoRequest
	) {
		Long userIdByJwt = userDetails.getUserId();
		actionService.updateActionInfo(updateActionInfoRequest, actionId, userIdByJwt);
		return ResponseEntity.noContent().build();
	}
}
