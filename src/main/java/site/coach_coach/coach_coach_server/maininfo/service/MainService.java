package site.coach_coach.coach_coach_server.maininfo.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import site.coach_coach.coach_coach_server.coach.dto.*;
import site.coach_coach.coach_coach_server.coach.repository.*;
import site.coach_coach.coach_coach_server.like.repository.*;
import site.coach_coach.coach_coach_server.maininfo.dto.*;
import site.coach_coach.coach_coach_server.sport.dto.*;
import site.coach_coach.coach_coach_server.sport.repository.*;


import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

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

	@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
	public MainResponseDto getMainResponse(Long userId) {
		List<SportDto> sports;
		List<CoachDto> coaches;

		try {
			sports = sportRepository.findAll().stream()
				.map(sport -> SportDto.builder()
					.sportId(sport.getSportId())
					.sportName(sport.getSportName())
					.sportImageUrl(sport.getSportImageUrl())
					.build())
				.collect(Collectors.toList());

			coaches = coachRepository.findAll().stream()
				.map(coach -> {
					List<CoachingSportDto> coachingSports = coach.getCoachingSports().stream()
						.map(cs -> CoachingSportDto.builder()
							.sportId(cs.getSport().getSportId())
							.sportName(cs.getSport().getSportName())
							.build())
						.collect(Collectors.toList());

					int countOfLikes = userCoachLikeRepository.countLikesByCoach_CoachId(coach.getCoachId());
					int recentLikes = userCoachLikeRepository.countLikesByCoachAndCreatedAtAfter(
						coach.getCoachId(), LocalDateTime.now().minusWeeks(1)
					);
					boolean liked = userId != null && userCoachLikeRepository.existsByUser_UserIdAndCoach_CoachId(userId, coach.getCoachId());

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
				})
				.sorted((c1, c2) -> Integer.compare(c2.getCountOfLikes(), c1.getCountOfLikes()))
				.limit(3)
				.collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching main response data", e);
		}

		return MainResponseDto.builder()
			.sports(sports)
			.coaches(coaches)
			.build();
	}
}
