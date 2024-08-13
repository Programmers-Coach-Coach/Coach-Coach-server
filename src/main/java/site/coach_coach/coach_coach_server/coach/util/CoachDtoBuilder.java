package site.coach_coach.coach_coach_server.coach.util;

import java.util.List;
import java.util.stream.Collectors;

import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.coach.dto.CoachDto;
import site.coach_coach.coach_coach_server.like.repository.UserCoachLikeRepository;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;
import site.coach_coach.coach_coach_server.user.domain.User;

public class CoachDtoBuilder {
	public static CoachDto buildCoachDto(Coach coach, User user, UserCoachLikeRepository userCoachLikeRepository) {
		List<CoachingSportDto> coachingSports = coach.getCoachingSports().stream()
			.map(cs -> new CoachingSportDto(
				cs.getSport().getSportId(),
				cs.getSport().getSportName()
			))
			.collect(Collectors.toList());

		int countOfLikes = getCountOfLikes(coach, userCoachLikeRepository);
		boolean liked = user != null && isLikedByUser(user, coach, userCoachLikeRepository);

		return new CoachDto(
			coach.getCoachId(),
			coach.getUser().getNickname(),
			coach.getUser().getProfileImageUrl(),
			coach.getCoachIntroduction(),
			countOfLikes,
			liked,
			coachingSports
		);
	}

	private static int getCountOfLikes(Coach coach, UserCoachLikeRepository userCoachLikeRepository) {
		return userCoachLikeRepository.countByCoach_CoachId(coach.getCoachId());
	}

	private static boolean isLikedByUser(User user, Coach coach, UserCoachLikeRepository userCoachLikeRepository) {
		return userCoachLikeRepository.existsByUser_UserIdAndCoach_CoachId(user.getUserId(), coach.getCoachId());
	}
}
