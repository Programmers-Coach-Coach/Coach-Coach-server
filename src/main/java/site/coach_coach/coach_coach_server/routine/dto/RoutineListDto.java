package site.coach_coach.coach_coach_server.routine.dto;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoutineListDto {
	private Optional<RoutineListCoachInfoDto> routineListCoachInfoDto;
	private List<RoutineForListDto> routineForListDtos;

}
