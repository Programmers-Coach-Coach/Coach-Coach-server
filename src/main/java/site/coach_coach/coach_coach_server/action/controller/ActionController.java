package site.coach_coach.coach_coach_server.action.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.action.service.ActionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActionController {
	private final ActionService actionService;

	// @PostMapping("/v2/categories/{categoryId}")
	// public ResponseEntity<SuccessIdResponse> createActionIntoCategory(
	// 	@AuthenticationPrincipal CustomUserDetails userDetails,
	// 	@PathVariable(name = "routineId") Long routineId,
	// 	@RequestBody @Valid CreateActionRequest createActionRequest
	// ) {
	// 	Long userIdByJwt = userDetails.getUserId();
	// 	Long actionId = actionService.createAction(routineId, userIdByJwt, createActionRequest);
	// 	return ResponseEntity.status(HttpStatus.CREATED)
	// 		.body(new SuccessIdResponse(actionId));
	// }

	// @DeleteMapping("/v2/actions/{actionId}")
	// public ResponseEntity<Void> deleteActionInCategory(
	// 	@AuthenticationPrincipal CustomUserDetails userDetails,
	// 	@PathVariable(name = "actionId") Long actionId
	// ) {
	// 	Long userIdByJwt = userDetails.getUserId();
	// 	actionService.deleteAction(actionId, userIdByJwt);
	// 	return ResponseEntity.noContent().build();
	// }

	// @PatchMapping("/v2/actions/{actionId}")
	// public ResponseEntity<Void> updateActionInfo(
	// 	@AuthenticationPrincipal CustomUserDetails userDetails,
	// 	@PathVariable(name = "actionId") Long actionId,
	// 	@RequestBody @Valid UpdateActionInfoRequest updateActionInfoRequest
	// ) {
	// 	Long userIdByJwt = userDetails.getUserId();
	// 	actionService.updateActionInfo(updateActionInfoRequest, actionId, userIdByJwt);
	// 	return ResponseEntity.noContent().build();
	// }
}
