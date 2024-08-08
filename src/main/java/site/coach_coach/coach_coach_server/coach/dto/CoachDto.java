package site.coach_coach.coach_coach_server.coach.dto;

import lombok.*;
import site.coach_coach.coach_coach_server.sport.dto.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachDto {
	private Long coachId;
	private String coachName;
	private String coachImageUrl;
	private String description;
	private int countOfLikes;
	private boolean liked;
	private List<CoachingSportDto> coachingSports;
	private int likes;
	private int recentLikes;
}
