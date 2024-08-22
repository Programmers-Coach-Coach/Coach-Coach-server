package site.coach_coach.coach_coach_server.recorddata.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.constants.SuccessMessage;
import site.coach_coach.coach_coach_server.common.response.SuccessResponse;
import site.coach_coach.coach_coach_server.recorddata.dto.RecordDataRequest;
import site.coach_coach.coach_coach_server.recorddata.service.RecordDataService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecordDataController {
	private final RecordDataService recordDataService;

	@PostMapping("/v1/records/{date}")
	public ResponseEntity<SuccessResponse> addRecordDate(
		@PathVariable("date") String date,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody RecordDataRequest recordDataRequest
	) {
		Long userId = userDetails.getUserId();
		recordDataService.createRecordDate(date, userId, recordDataRequest);

		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.CREATE_RECORD_DATA_SUCCESS.getMessage())
		);
	}
}
