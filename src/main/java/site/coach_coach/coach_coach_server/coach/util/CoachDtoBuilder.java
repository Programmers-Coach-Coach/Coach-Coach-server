package site.coach_coach.coach_coach_server.coach.util;

import java.time.LocalDateTime;
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
			.map(cs -> CoachingSportDto.builder()
				.sportId(cs.getSport().getSportId())
				.sportName(cs.getSport().getSportName())
				.build())
			.collect(Collectors.toList());

		int countOfLikes = getCountOfLikesSince(coach, userCoachLikeRepository);
		boolean liked = user != null && isLikedByUser(user, coach, userCoachLikeRepository);

		return CoachDto.builder()
			.coachId(coach.getCoachId())
			.coachName(coach.getUser().getNickname())
			.profileImageUrl(coach.getUser().getProfileImageUrl())
			.description(coach.getCoachIntroduction())
			.countOfLikes(countOfLikes)
			.liked(liked)
			.coachingSports(coachingSports)
			.build();
	}

	private static int getCountOfLikesSince(Coach coach, UserCoachLikeRepository userCoachLikeRepository) {
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
		return userCoachLikeRepository.countLikesByCoachAndCreatedAtAfter(coach.getCoachId(), oneWeekAgo);
	}

	private static boolean isLikedByUser(User user, Coach coach, UserCoachLikeRepository userCoachLikeRepository) {
		return userCoachLikeRepository.existsByUser_UserIdAndCoach_CoachId(user.getUserId(), coach.getCoachId());
	}
}
