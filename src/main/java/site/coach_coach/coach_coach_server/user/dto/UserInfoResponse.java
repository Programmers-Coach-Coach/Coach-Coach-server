package site.coach_coach.coach_coach_server.user.dto;

import java.util.List;

import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;
import site.coach_coach.coach_coach_server.sport.domain.InterestedSport;

@Builder
public record UserInfoResponse(
	String nickname,
	String email,
	String profileImageUrl,
	GenderEnum gender,
	String localAddress,
	String localAddressDetail,
	String introduction,
	List<InterestedSport> interestedSports
) {
}
