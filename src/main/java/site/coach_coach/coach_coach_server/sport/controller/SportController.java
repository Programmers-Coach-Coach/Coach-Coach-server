package site.coach_coach.coach_coach_server.sport.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;
import site.coach_coach.coach_coach_server.sport.service.SportService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SportController {

	private final SportService sportService;

	@GetMapping("/sports")
	public ResponseEntity<List<SportDto>> getSports() {
		List<SportDto> sports = sportService.getAllSports();
		return ResponseEntity.ok(sports);
	}
}
