package site.coach_coach.coach_coach_server.maininfo.dto;

import lombok.*;
import site.coach_coach.coach_coach_server.coach.dto.*;
import site.coach_coach.coach_coach_server.sport.dto.*;

import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainResponseDto {
	private List<SportDto> sports;
	private List<CoachDto> coaches;
}
