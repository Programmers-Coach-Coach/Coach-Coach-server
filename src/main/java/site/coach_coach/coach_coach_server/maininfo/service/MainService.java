package site.coach_coach.coach_coach_server.maininfo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.coach.dto.CoachDto;
import site.coach_coach.coach_coach_server.coach.util.CoachDtoBuilder;
import site.coach_coach.coach_coach_server.like.repository.UserCoachLikeRepository;
import site.coach_coach.coach_coach_server.maininfo.dto.MainResponseDto;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;
import site.coach_coach.coach_coach_server.user.domain.User;

@Service
@Transactional(readOnly = true)
public class MainService {

	private final SportRepository sportRepository;
	private final UserCoachLikeRepository userCoachLikeRepository;

	@Autowired
	public MainService(
		SportRepository sportRepository,
		UserCoachLikeRepository userCoachLikeRepository
	) {
		this.sportRepository = sportRepository;
		this.userCoachLikeRepository = userCoachLikeRepository;
	}

	public MainResponseDto getMainResponse(User user) {
		try {
			List<SportDto> sports = getSports();
			List<CoachDto> coaches = getTopCoaches(user);
			return new MainResponseDto(sports, coaches);
		} catch (Exception e) {
			throw new RuntimeException("Error fetching main response data", e);
		}
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

	private List<CoachDto> getTopCoaches(User user) {
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
		List<Coach> topCoaches = userCoachLikeRepository.findTopCoachesByLikesSince(oneWeekAgo, PageRequest.of(0, 3));
		return topCoaches.stream()
			.map(coach -> CoachDtoBuilder.buildCoachDto(coach, user, userCoachLikeRepository))
			.collect(Collectors.toList());
	}
}
