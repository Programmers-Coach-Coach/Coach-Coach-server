package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;

public record CoachRequest(

	@NotBlank
	String coachIntroduction,

	String activeCenter,

	String activeCenterDetail,

	@NotBlank
	String activeHours,

	@NotBlank
	String chattingUrl,

	@NotNull
	Boolean isOpen,

	List<CoachingSportDto> coachingSports
) {

}
