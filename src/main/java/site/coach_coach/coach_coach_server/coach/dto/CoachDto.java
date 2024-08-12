package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachDto {
	private Long coachId;
	private String coachName;
	private String profileImageUrl;
	private String description;
	private int countOfLikes;
	private boolean liked;
	private List<CoachingSportDto> coachingSports;
}
