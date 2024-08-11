package site.coach_coach.coach_coach_server.maininfo.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import site.coach_coach.coach_coach_server.coach.domain.*;
import site.coach_coach.coach_coach_server.coach.dto.*;
import site.coach_coach.coach_coach_server.coach.repository.*;
import site.coach_coach.coach_coach_server.like.repository.*;
import site.coach_coach.coach_coach_server.maininfo.dto.*;
import site.coach_coach.coach_coach_server.sport.dto.*;
import site.coach_coach.coach_coach_server.sport.repository.*;
import site.coach_coach.coach_coach_server.user.domain.User;

import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
public class MainService {

	private final SportRepository sportRepository;
	private final CoachRepository coachRepository;
	private final UserCoachLikeRepository userCoachLikeRepository;

	@Autowired
	public MainService(SportRepository sportRepository, CoachRepository coachRepository, UserCoachLikeRepository userCoachLikeRepository) {
		this.sportRepository = sportRepository;
		this.coachRepository = coachRepository;
		this.userCoachLikeRepository = userCoachLikeRepository;
	}

	public MainResponseDto getMainResponse(User user) {
		try {
			List<SportDto> sports = getSports();
			List<CoachDto> coaches = getTopCoaches(user);
			return MainResponseDto.builder()
				.sports(sports)
				.coaches(coaches)
				.build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching main response data", e);
		}
	}

	private List<SportDto> getSports() {
		return sportRepository.findAll().stream()
			.map(sport -> SportDto.builder()
				.sportId(sport.getSportId())
				.sportName(sport.getSportName())
				.sportImageUrl(sport.getSportImageUrl())
				.build())
			.collect(Collectors.toList());
	}

	private List<CoachDto> getTopCoaches(User user) {
		return coachRepository.findAll().stream()
			.map(coach -> buildCoachDto(coach, user))
			.sorted((c1, c2) -> Integer.compare(c2.getCountOfLikes(), c1.getCountOfLikes()))
			.limit(3)
			.collect(Collectors.toList());
	}

	private CoachDto buildCoachDto(Coach coach, User user) {
		List<CoachingSportDto> coachingSports = coach.getCoachingSports().stream()
			.map(cs -> CoachingSportDto.builder()
				.sportId(cs.getSport().getSportId())
				.sportName(cs.getSport().getSportName())
				.build())
			.collect(Collectors.toList());

		int countOfLikes = getCountOfLikes(coach);
		int recentLikes = getRecentLikes(coach);
		boolean liked = user != null && isLikedByUser(user, coach);

		return CoachDto.builder()
			.coachId(coach.getCoachId())
			.coachName(coach.getUser().getNickname())
			.coachImageUrl(coach.getUser().getProfileImageUrl())
			.description(coach.getCoachIntroduction())
			.countOfLikes(countOfLikes)
			.liked(liked)
			.coachingSports(coachingSports)
			.likes(countOfLikes)
			.recentLikes(recentLikes)
			.build();
	}

	private int getCountOfLikes(Coach coach) {
		return userCoachLikeRepository.countLikesByCoach_CoachId(coach.getCoachId());
	}

	private int getRecentLikes(Coach coach) {
		return userCoachLikeRepository.countLikesByCoachAndCreatedAtAfter(
			coach.getCoachId(), LocalDateTime.now().minusWeeks(1)
		);
	}

	private boolean isLikedByUser(User user, Coach coach) {
		return userCoachLikeRepository.existsByUser_UserIdAndCoach_CoachId(user.getUserId(), coach.getCoachId());
	}
}
