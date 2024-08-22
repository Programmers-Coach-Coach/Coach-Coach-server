package site.coach_coach.coach_coach_server.recorddata.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.recorddata.domain.RecordData;

@Repository
public interface RecordDataRepository extends JpaRepository<RecordData, Long> {
	Optional<RecordData> findByRecordDateAndUser_UserId(Date date, Long userId);
}
