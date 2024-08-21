package site.coach_coach.coach_coach_server.coach.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.coach.dto.CoachDetailDto;
import site.coach_coach.coach_coach_server.coach.dto.CoachListResponse;
import site.coach_coach.coach_coach_server.coach.dto.CreateContactResponse;
import site.coach_coach.coach_coach_server.coach.service.CoachService;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;

@RestController
@RequestMapping("/api")
public class CoachController {

	private final CoachService coachService;

	public CoachController(CoachService coachService) {
		this.coachService = coachService;
	}

	@GetMapping("/v1/coaches")
	public ResponseEntity<CoachDetailDto> getCoachDetail(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "coachId") Long coachId
	) {
		if (userDetails == null || userDetails.getUser() == null) {
			throw new InvalidUserException();
		}
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

	@PostMapping("/v1/coaches/{coachId}/contact")
	public ResponseEntity<CreateContactResponse> contactCoach(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long coachId
	) {
		User user = userDetails.getUser();
		Long matchingId = coachService.contactCoach(user, coachId);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new CreateContactResponse(HttpStatus.CREATED.value(), matchingId));
	}
}
