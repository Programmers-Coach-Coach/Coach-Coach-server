package site.coach_coach.coach_coach_server.sport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import site.coach_coach.coach_coach_server.sport.domain.InterestedSport;

@Builder
public record InterestedSportDto(
	Long sportId,
	@NotBlank
	String sportName
) {
	public static InterestedSportDto from(InterestedSport interestedSport) {
		return new InterestedSportDto(
			interestedSport.getSport().getSportId(),
			interestedSport.getSport().getSportName()
		);
	}
}
