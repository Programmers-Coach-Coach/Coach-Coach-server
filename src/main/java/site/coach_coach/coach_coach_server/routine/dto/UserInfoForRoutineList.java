package site.coach_coach.coach_coach_server.routine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserInfoForRoutineList(
	Long userId,

	@NotBlank
	@Size(max = 45)
	String nickname,

	@Size(max = 500)
	String profileImageUrl
) {

}
