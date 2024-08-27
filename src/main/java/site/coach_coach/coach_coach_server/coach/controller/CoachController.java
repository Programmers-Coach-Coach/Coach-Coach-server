package site.coach_coach.coach_coach_server.coach.controller;

import java.util.List;

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
import site.coach_coach.coach_coach_server.coach.dto.MatchingCoachResponseDto;
import site.coach_coach.coach_coach_server.coach.dto.MatchingUserResponseDto;
import site.coach_coach.coach_coach_server.coach.dto.ReviewRequestDto;
import site.coach_coach.coach_coach_server.coach.service.CoachService;
import site.coach_coach.coach_coach_server.common.constants.SuccessMessage;
import site.coach_coach.coach_coach_server.common.exception.UserNotFoundException;
import site.coach_coach.coach_coach_server.common.response.SuccessResponse;
import site.coach_coach.coach_coach_server.user.domain.User;

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
			throw new UserNotFoundException();
		}

		User user = userDetails.getUser();
		CoachListResponse response = coachService.getAllCoaches(user, page, sports, search, latest, review, liked, my);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/v1/coaches/matches/{userId}")
	public ResponseEntity<SuccessResponse> matchMember(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "userId") Long userId) {

		Long coachUserId = userDetails.getUser().getUserId();
		coachService.updateMatchingStatus(coachUserId, userId);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessResponse(HttpStatus.CREATED.value(), SuccessMessage.MATCH_MEMBER_SUCCESS.getMessage()));
	}

	@PostMapping("/v1/coaches/{coachId}/contact")
	public ResponseEntity<SuccessResponse> contactCoach(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "coachId") Long coachId
	) {
		User user = userDetails.getUser();
		coachService.contactCoach(user, coachId);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessResponse(HttpStatus.CREATED.value(), SuccessMessage.CREATE_CONTACT_SUCCESS.getMessage()));
	}

	@DeleteMapping("/v1/coaches/matches/{userId}")
	public ResponseEntity<SuccessResponse> deleteMatching(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "userId") Long userId) {

		Long coachUserId = userDetails.getUser().getUserId();
		coachService.deleteMatching(coachUserId, userId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.DELETE_MATCHING.getMessage()));
	}

	@PostMapping("/v1/coaches/{coachId}/likes")
	public ResponseEntity<SuccessResponse> addCoachToFavorites(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "coachId") Long coachId) {

		Long userId = userDetails.getUserId();
		coachService.addCoachToFavorites(userId, coachId);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessResponse(HttpStatus.CREATED.value(), SuccessMessage.CREATE_LIKE_SUCCESS.getMessage()));
	}

	@DeleteMapping("/v1/coaches/{coachId}/likes")
	public ResponseEntity<SuccessResponse> deleteCoachToFavorites(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "coachId") Long coachId) {

		Long userId = userDetails.getUserId();
		coachService.deleteCoachToFavorites(userId, coachId);
		return ResponseEntity.ok()
			.body(new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.DELETE_LIKE_SUCCESS.getMessage()));
	}

	@GetMapping("/v1/coaches/matches")
	public ResponseEntity<List<MatchingUserResponseDto>> getMatchingUsers(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long coachUserId = userDetails.getUser().getUserId();
		List<MatchingUserResponseDto> matchingUsers = coachService.getMatchingUsersByCoachId(coachUserId);

		return ResponseEntity.ok(matchingUsers);
	}

	@GetMapping("/v1/users/matches")
	public ResponseEntity<List<MatchingCoachResponseDto>> getMatchingCoaches(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = userDetails.getUserId();
		List<MatchingCoachResponseDto> matchingCoaches = coachService.getMatchingCoachesByUserId(userId);

		return ResponseEntity.ok(matchingCoaches);
	}

	@PostMapping("/v1/coaches/{coachId}/reviews")
	public ResponseEntity<SuccessResponse> addReview(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "coachId") Long coachId,
		@RequestBody @Valid ReviewRequestDto requestDto) {

		Long userId = userDetails.getUserId();
		coachService.addReview(userId, coachId, requestDto);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessResponse(HttpStatus.CREATED.value(), SuccessMessage.CREATE_REVIEW_SUCCESS.getMessage()));
	}

	@DeleteMapping("/v1/coaches/reviews/{reviewId}")
	public ResponseEntity<SuccessResponse> deleteReview(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "reviewId") Long reviewId) {

		Long userId = userDetails.getUserId();
		coachService.deleteReview(reviewId, userId);

		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.DELETE_REVIEW_SUCCESS.getMessage()));
	}
}
