package ru.itis.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.entity.Lesson;
import ru.itis.project.entity.Skill;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long> {

    void save(Lesson lesson);

    Optional<Lesson> findById(Long id);

    int countAllByTeacherSkillId(Long id);

    @Query("UPDATE Lesson l SET l.status = :status WHERE l.id = :id")
    void updateStatus(Long id, LessonStatus status);

    @Query("SELECT l " +
            "FROM Lesson l " +
            "JOIN FETCH User t ON l.teacher=t " +
            "JOIN FETCH User s ON l.student=s " +
            "WHERE (t.username=:username OR s.username=:username) " +
            "AND (l.status=:status)")
    List<Lesson> findAllByTeacherUsernameAndStudentUsernameAndStatus(String username, LessonStatus status);

    @Query("SELECT l " +
            "FROM Lesson l " +
            "JOIN FETCH User t ON l.teacher=t " +
            "JOIN FETCH User s ON l.student=s " +
            "WHERE (t.username=:username OR s.username=:username)"
    )
    List<Lesson> findAllByTeacherUsernameAndStudentUsername(String username, Pageable pageable);
}
