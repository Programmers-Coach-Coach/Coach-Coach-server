package site.coach_coach.coach_coach_server.user.dto;

import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;
import site.coach_coach.coach_coach_server.sport.dto.InterestedSportDto;
import site.coach_coach.coach_coach_server.user.validation.Nickname;

@Builder
public record UserProfileRequest(
	@Nickname
	String nickname,

	GenderEnum gender,

	@Size(max = 100)
	String localAddress,

	@Size(max = 100)
	String localAddressDetail,

	@Size(max = 1000)
	String introduction,

	List<InterestedSportDto> interestedSports
) {
}
