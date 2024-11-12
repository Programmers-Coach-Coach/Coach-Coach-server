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

	public List<SportDto> getSports() {
		return sportRepository.findAll().stream()
			.map(sport -> new SportDto(
				sport.getSportId(),
				sport.getSportName(),
				sport.getSportImageUrl()
			))
			.collect(Collectors.toList());
	}

	public List<MainInfoCoachDto> getTopCoaches(User user) {
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
		List<Coach> topCoaches = userCoachLikeRepository.findTopCoachesByLikesSinceAndIsOpenTrue(
			oneWeekAgo, PageRequest.of(0, 3));

		return topCoaches.stream()
			.map(coach -> buildMainInfoCoachDto(coach, user))
			.collect(Collectors.toList());
	}

	private MainInfoCoachDto buildMainInfoCoachDto(Coach coach, User user) {
		List<CoachingSportDto> coachingSports = coach.getCoachingSports().stream()
			.map(cs -> new CoachingSportDto(
				cs.getSport().getSportId(),
				cs.getSport().getSportName()
			))
			.collect(Collectors.toList());

		int countOfLikes = getCountOfLikes(coach);
		boolean isLiked = isLikedByUser(user, coach);

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

	private int getCountOfLikes(Coach coach) {
		return userCoachLikeRepository.countByCoach_CoachId(coach.getCoachId());
	}

	private boolean isLikedByUser(User user, Coach coach) {
		return userCoachLikeRepository.existsByUser_UserIdAndCoach_CoachId(user.getUserId(), coach.getCoachId());
	}
}
