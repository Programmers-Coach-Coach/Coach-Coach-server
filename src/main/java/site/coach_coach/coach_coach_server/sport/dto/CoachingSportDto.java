package site.coach_coach.coach_coach_server.sport.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachingSportDto {
	private Long sportId;
	private String sportName;
}
