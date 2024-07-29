package site.coach_coach.coach_coach_server.coach.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.coach.dto.CoachDto;
import site.coach_coach.coach_coach_server.coach.dto.StartedAsCoachDto;
import site.coach_coach.coach_coach_server.coach.exception.InvalidUserIdException;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;

@Service
@RequiredArgsConstructor
public class CoachService {
	private final CoachRepository coachRepository;

	@Transactional
	public void startedAsCoach(StartedAsCoachDto startedAsCoachDto) {
		if (coachRepository.existsByUserId(startedAsCoachDto.userId())) {
			throw new InvalidUserIdException();
		}
		Coach coach = buildCoach(startedAsCoachDto);
		coachRepository.save(coach);
		CoachDto.from(coach);
	}

	private Coach buildCoach(StartedAsCoachDto startedAsCoachDto) {
		return Coach.builder()
			.userId(startedAsCoachDto.userId()).build();
	}
}
