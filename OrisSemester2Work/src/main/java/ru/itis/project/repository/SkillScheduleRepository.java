package ru.itis.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.project.entity.SkillSchedule;

import java.time.LocalDate;
import java.util.List;

public interface SkillScheduleRepository extends JpaRepository<SkillSchedule, Long> {

    List<SkillSchedule> findAllBySkillId(Long skillId);

    @Query("SELECT s FROM SkillSchedule s " +
            "WHERE s.skill.id = :skillId " +
            "AND (s.singleDate IS NULL OR s.singleDate > :now)")
    List<SkillSchedule> findUpcomingOrUnboundSchedules(Long skillId, LocalDate now);

    void deleteAllBySkillId(Long skillId);

}
