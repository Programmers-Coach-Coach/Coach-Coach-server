package site.coach_coach.coach_coach_server.user.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;
import site.coach_coach.coach_coach_server.common.validation.Enum;
import site.coach_coach.coach_coach_server.sport.domain.InterestedSport;
import site.coach_coach.coach_coach_server.user.validation.Nickname;

@Builder
public record UserProfileRequest(
	@Nickname
	String nickname,

	MultipartFile profileImage,

	@Enum(enumClass = GenderEnum.class)
	GenderEnum gender,

	@Size(max = 100)
	String localAddress,

	@Size(max = 100)
	String localAddressDetail,

	@Lob
	String introduction,

	List<InterestedSport> interestedSports
) {
}
