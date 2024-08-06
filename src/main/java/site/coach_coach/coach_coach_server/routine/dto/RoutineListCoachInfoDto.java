package site.coach_coach.coach_coach_server.routine.dto;

import lombok.Builder;

@Builder
public record RoutineListCoachInfoDto(
	Long coachId,

	String nickname,

	String profileImageUrl) {

}
