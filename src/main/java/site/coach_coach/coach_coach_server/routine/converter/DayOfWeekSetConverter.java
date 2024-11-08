package site.coach_coach.coach_coach_server.routine.converter;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DayOfWeekSetConverter implements AttributeConverter<Set<DayOfWeek>, String> {

	@Override
	public String convertToDatabaseColumn(Set<DayOfWeek> attribute) {
		return String.join(",", attribute.stream().map(DayOfWeek::name).toArray(String[]::new));
	}

	@Override
	public Set<DayOfWeek> convertToEntityAttribute(String dbData) {
		return Arrays.stream(dbData.split(","))
			.map(DayOfWeek::valueOf)
			.collect(Collectors.toSet());
	}
}
