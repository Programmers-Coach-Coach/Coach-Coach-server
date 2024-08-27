package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;

public record CoachRequest(

	@NotNull
	String coachIntroduction,
	@Size(max = 100)
	String activeCenter,
	@Size(max = 100)
	String activeCenterDetail,
	@NotBlank
	@Size(max = 100)
	String activeHours,
	@NotBlank
	@Size(max = 400)
	String chattingUrl,
	@NotNull
	Boolean isOpen,
	List<CoachingSportDto> coachingSports
) {

}
