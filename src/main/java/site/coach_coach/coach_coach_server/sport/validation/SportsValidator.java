package site.coach_coach.coach_coach_server.sport.validation;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;
import site.coach_coach.coach_coach_server.sport.domain.Sport;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;

public class SportsValidator implements ConstraintValidator<Sports, Long[]> {

	private final SportRepository sportRepository;

	public SportsValidator(SportRepository sportRepository) {
		this.sportRepository = sportRepository;
	}

	public boolean isValid(Long[] sports, ConstraintValidatorContext context) {
		if (sports == null || sports.length == 0) {
			return true;
		}

		List<Long> sportIds = Arrays.asList(sports);
		List<Sport> existingSports = sportRepository.findAllById(sportIds);

		if (existingSports.size() != sportIds.size()) {
			throw new NotFoundException(ErrorMessage.NOT_FOUND_SPORTS);
		}

		return true;
	}
}
