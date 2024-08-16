package site.coach_coach.coach_coach_server.user.dto;

import java.util.List;

import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;
import site.coach_coach.coach_coach_server.sport.domain.InterestedSport;
import site.coach_coach.coach_coach_server.user.domain.User;

@Builder
public record UserProfileResponse(
	String email,
	String nickname,
	String profileImageUrl,
	GenderEnum gender,
	String localAddress,
	String localAddressDetail,
	String introduction,
	List<InterestedSport> interestedSports
) {
	public static UserProfileResponse from(User user) {
		return UserProfileResponse.builder()
			.email(user.getEmail())
			.nickname(user.getNickname())
			.profileImageUrl(user.getProfileImageUrl())
			.gender(user.getGender())
			.localAddress(user.getLocalAddress())
			.localAddressDetail(user.getLocalAddressDetail())
			.introduction(user.getIntroduction())
			.interestedSports(user.getInterestedSports())
			.build();
	}
}
