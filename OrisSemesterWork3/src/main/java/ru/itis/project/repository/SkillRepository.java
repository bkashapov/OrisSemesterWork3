package ru.itis.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.project.entity.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
}
