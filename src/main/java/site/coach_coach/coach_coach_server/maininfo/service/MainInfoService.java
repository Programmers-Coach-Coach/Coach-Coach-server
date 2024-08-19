package site.coach_coach.coach_coach_server.maininfo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.like.repository.UserCoachLikeRepository;
import site.coach_coach.coach_coach_server.maininfo.dto.MainInfoCoachDto;
import site.coach_coach.coach_coach_server.maininfo.dto.MainInfoResponseDto;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;
import site.coach_coach.coach_coach_server.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainInfoService {

	private final SportRepository sportRepository;
	private final UserCoachLikeRepository userCoachLikeRepository;

	public MainInfoResponseDto getMainInfoResponse(User user) {
		List<SportDto> sports = getSports();
		List<MainInfoCoachDto> coaches = getTopCoaches(user);

		return new MainInfoResponseDto(sports, coaches);
	}

	private List<SportDto> getSports() {
		return sportRepository.findAll().stream()
			.map(sport -> new SportDto(
				sport.getSportId(),
				sport.getSportName(),
				sport.getSportImageUrl()
			))
			.collect(Collectors.toList());
	}

	private List<MainInfoCoachDto> getTopCoaches(User user) {
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
		List<Coach> topCoaches = userCoachLikeRepository.findTopCoachesByLikesSince(oneWeekAgo, PageRequest.of(0, 3));
		return topCoaches.stream()
			.map(coach -> buildMainInfoCoachDto(coach, user, userCoachLikeRepository))
			.collect(Collectors.toList());
	}

	public MainInfoCoachDto buildMainInfoCoachDto(Coach coach, User user,
		UserCoachLikeRepository userCoachLikeRepository) {
		List<CoachingSportDto> coachingSports = coach.getCoachingSports().stream()
			.map(cs -> new CoachingSportDto(
				cs.getSport().getSportId(),
				cs.getSport().getSportName()
			))
			.collect(Collectors.toList());

		int countOfLikes = getCountOfLikes(coach, userCoachLikeRepository);

		boolean isLiked = isLikedByUser(user, coach, userCoachLikeRepository);

		return new MainInfoCoachDto(
			coach.getCoachId(),
			coach.getUser().getNickname(),
			coach.getUser().getProfileImageUrl(),
			coach.getCoachIntroduction(),
			countOfLikes,
			isLiked,
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
