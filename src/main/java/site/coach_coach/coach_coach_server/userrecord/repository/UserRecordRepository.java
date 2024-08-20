package site.coach_coach.coach_coach_server.userrecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;

public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
}
