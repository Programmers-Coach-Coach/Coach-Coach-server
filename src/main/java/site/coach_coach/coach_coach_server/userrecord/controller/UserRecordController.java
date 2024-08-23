package site.coach_coach.coach_coach_server.userrecord.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.response.SuccessIdResponse;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordCreateRequest;
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
		return ResponseEntity.ok(
			new SuccessIdResponse(recordId)
		);
	}
}
