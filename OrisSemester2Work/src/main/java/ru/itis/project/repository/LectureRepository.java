package ru.itis.project.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itis.project.entity.Lecture;

public interface LectureRepository extends PagingAndSortingRepository<Lecture, Long> {

    int countAllBySkillId(Long skillId);
}
