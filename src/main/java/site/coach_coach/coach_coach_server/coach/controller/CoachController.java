package site.coach_coach.coach_coach_server.coach.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.coach.dto.CoachDetailDto;
import site.coach_coach.coach_coach_server.coach.dto.CoachListResponse;
import site.coach_coach.coach_coach_server.coach.dto.CoachRequest;
import site.coach_coach.coach_coach_server.coach.service.CoachService;
import site.coach_coach.coach_coach_server.common.constants.SuccessMessage;
import site.coach_coach.coach_coach_server.common.response.SuccessResponse;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CoachController {

	private final CoachService coachService;

	@PostMapping("/v1/coaches")
	public ResponseEntity<SuccessResponse> createOrUpdateCoach(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid CoachRequest coachRequest) {

		Long userId = userDetails.getUserId();
		coachService.saveOrUpdateCoach(userId, coachRequest);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessResponse(HttpStatus.CREATED.value(),
				SuccessMessage.UPDATE_COACH_PROFILE_SUCCESS.getMessage()));
	}

	@GetMapping("/v1/coaches")
	public ResponseEntity<CoachDetailDto> getCoachDetail(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "coachId", required = false) Long coachId
	) {
		User user = userDetails.getUser();
		CoachDetailDto response = coachService.getCoachDetail(user, coachId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/v1/coaches-all")
	public ResponseEntity<CoachListResponse> getAllCoaches(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(required = false) String sports,
		@RequestParam(required = false) String search,
		@RequestParam(required = false) Boolean latest,
		@RequestParam(required = false) Boolean review,
		@RequestParam(required = false) Boolean liked,
		@RequestParam(required = false) Boolean my
	) {
		if (userDetails == null || userDetails.getUser() == null) {
			throw new InvalidUserException();
		}

		User user = userDetails.getUser();
		CoachListResponse response = coachService.getAllCoaches(user, page, sports, search, latest, review, liked, my);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/v1/coaches/matches/{userId}")
	public ResponseEntity<SuccessResponse> matchMember(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long userId) {

		Long coachUserId = userDetails.getUser().getUserId();
		coachService.updateMatchingStatus(coachUserId, userId);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessResponse(HttpStatus.CREATED.value(), SuccessMessage.MATCH_MEMBER_SUCCESS.getMessage()));
	}

	@PostMapping("/v1/coaches/{coachId}/contact")
	public ResponseEntity<SuccessResponse> contactCoach(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long coachId
	) {
		User user = userDetails.getUser();
		coachService.contactCoach(user, coachId);
		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.CREATE_CONTACT_SUCCESS.getMessage()));
	}

	@DeleteMapping("/v1/coaches/{coachId}/likes")
	public ResponseEntity<SuccessResponse> deleteCoachToFavorites(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long coachId) {

		Long userId = userDetails.getUserId();
		coachService.deleteCoachToFavorites(userId, coachId);
		return ResponseEntity.ok()
			.body(new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.DELETE_LIKE_SUCCESS.getMessage()));
	}
}
