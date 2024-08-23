package site.coach_coach.coach_coach_server.userrecord.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.response.SuccessIdResponse;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordCreateRequest;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordResponse;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordUpdateRequest;
import site.coach_coach.coach_coach_server.userrecord.service.UserRecordService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRecordController {
	private final UserRecordService userRecordService;

	@PostMapping("/v1/records")
	public ResponseEntity<SuccessIdResponse> addBodyInfoToUserRecord(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid UserRecordCreateRequest userRecordCreateRequest
	) {
		Long userId = userDetails.getUserId();
		Long recordId = userRecordService.addBodyInfoToUserRecord(userId, userRecordCreateRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessIdResponse(recordId));
	}

	@PutMapping("/v1/records/{recordId}")
	public ResponseEntity<Void> updateBodyInfoToUserRecord(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable("recordId") @Positive Long recordId,
		@RequestBody @Valid UserRecordUpdateRequest userRecordUpdateRequest
	) {
		Long userId = userDetails.getUserId();
		userRecordService.updateBodyInfoToUserRecord(userId, recordId, userRecordUpdateRequest);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/v1/records")
	public ResponseEntity<UserRecordResponse> getRecords(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "year") @NotNull int year,
		@RequestParam(name = "month") @NotNull @Min(1) @Max(12) int month
	) {
		Long userId = userDetails.getUserId();
		UserRecordResponse userRecordResponse = userRecordService.getUserRecords(userId, year, month);
		return ResponseEntity.ok(userRecordResponse);
	}
}
