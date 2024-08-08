package site.coach_coach.coach_coach_server.sport.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SportDto {
	private Long sportId;
	private String sportName;
	private String sportImageUrl;
}
