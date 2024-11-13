package site.coach_coach.coach_coach_server.userrecord.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.response.SuccessIdResponse;
import site.coach_coach.coach_coach_server.userrecord.dto.BodyInfoChartResponse;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordCreateRequest;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordDetailResponse;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordResponse;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordUpdateRequest;
import site.coach_coach.coach_coach_server.userrecord.service.UserRecordService;

@Validated
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
	public ResponseEntity<SuccessIdResponse> updateBodyInfoToUserRecord(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "recordId") @Positive Long recordId,
		@RequestBody @Valid UserRecordUpdateRequest userRecordUpdateRequest
	) {
		Long userId = userDetails.getUserId();
		userRecordService.updateBodyInfoToUserRecord(userId, recordId, userRecordUpdateRequest);
		return ResponseEntity.ok(new SuccessIdResponse(recordId));
	}

	@PostMapping("/v2/records")
	public ResponseEntity<Void> addBodyInfoToUserRecordV2(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "record_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate,
		@RequestBody @Valid UserRecordUpdateRequest userRecordupdateRequest
	) {
		Long userId = userDetails.getUserId();
		userRecordService.upsertBodyInfoToUserRecord(userId, recordDate, userRecordupdateRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/v1/records")
	public ResponseEntity<UserRecordResponse> getUserRecordsWithCompletionStatus(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "year") @NotNull Integer year,
		@RequestParam(name = "month") @NotNull @Min(1) @Max(12) Integer month
	) {
		Long userId = userDetails.getUserId();
		UserRecordResponse userRecordResponse = userRecordService.getUserRecordsByUserAndPeriod(userId, year, month);
		return ResponseEntity.ok(userRecordResponse);
	}

	@GetMapping("/v1/records/{recordId}")
	public ResponseEntity<UserRecordDetailResponse> getUserRecordDetail(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "recordId") @Positive Long recordId
	) {
		Long userId = userDetails.getUserId();
		UserRecordDetailResponse detailResponse = userRecordService.getUserRecordDetail(userId, recordId);
		return ResponseEntity.ok(detailResponse);
	}

	@GetMapping("/v2/records")
	public ResponseEntity<UserRecordDetailResponse> getUserRecordDetailV2(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "record_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate
	) {
		Long userId = userDetails.getUserId();
		UserRecordDetailResponse detailResponse = userRecordService.getUserRecordDetailV2(
			userId, recordDate
		);
		return ResponseEntity.ok(detailResponse);
	}

	@GetMapping("/v1/records/charts")
	public ResponseEntity<List<BodyInfoChartResponse>> getBodyInfoCharts(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "type") @NotBlank String type
	) {
		Long userId = userDetails.getUserId();
		List<BodyInfoChartResponse> response = userRecordService.getBodyInfoChart(userId, type);
		return ResponseEntity.ok(response);
	}
}
