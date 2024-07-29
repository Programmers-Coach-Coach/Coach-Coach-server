package site.coach_coach.coach_coach_server.coach.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import site.coach_coach.coach_coach_server.coach.domain.Coach;

@Builder
public record CoachDto(
	Long coachId,
	Long userId,
	LocalDateTime createdAt,
	String coachIntroduction,
	String activeCenter,
	Integer activeHoursOn,
	Integer activeHoursOff,
	String chattingUrl,
	Boolean isOpen
) {
	public static void from(Coach coach) {
		CoachDto.builder()
			.coachId(coach.getCoachId())
			.userId(coach.getUserId())
			.createdAt(coach.getCreatedAt())
			.coachIntroduction(coach.getCoachIntroduction())
			.activeCenter(coach.getActiveCenter())
			.activeHoursOn(coach.getActiveHoursOn())
			.activeHoursOff(coach.getActiveHoursOff())
			.chattingUrl(coach.getChattingUrl())
			.isOpen(coach.getIsOpen())
			.build();
	}
}
