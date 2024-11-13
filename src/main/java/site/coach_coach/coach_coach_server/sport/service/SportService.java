package site.coach_coach.coach_coach_server.sport.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SportService {

	private final SportRepository sportRepository;

	public List<SportDto> getAllSports() {
		return sportRepository.findAll().stream()
			.map(sport -> new SportDto(sport.getSportId(), sport.getSportName(), sport.getSportImageUrl()))
			.collect(Collectors.toList());
	}
}
