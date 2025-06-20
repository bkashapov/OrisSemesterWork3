package ru.itis.project.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.entity.Lesson;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long> {


    @Query("SELECT l FROM Lesson l " +
            "WHERE l.teacher.username = :teacherUsername " +
            "AND l.status != ru.itis.project.dictionary.LessonStatus.PENDED " +
            "AND l.status != ru.itis.project.dictionary.LessonStatus.REJECTED " +
            "AND l.startDateTime < :boundDate")
    List<Lesson> findByTeacherUsernameWithBoundDateAndStatusNotPendedAndNotRejected(String teacherUsername, LocalDateTime boundDate);


    void save(Lesson lesson);

    Optional<Lesson> findById(Long id);

    @Query("SELECT COUNT(l) " +
            "FROM Lesson l " +
            "WHERE l.status = ru.itis.project.dictionary.LessonStatus.COMPLETE")
    int countCompleteByTeacherSkillId(Long id);

    @Query("SELECT l " +
            "FROM Lesson l " +
            "JOIN FETCH User t ON l.teacher=t " +
            "JOIN FETCH User s ON l.student=s " +
            "WHERE (t.username=:username OR s.username=:username) " +
            "AND (l.status=:status)")
    List<Lesson> findAllByTeacherUsernameAndStudentUsernameAndStatus(String username, LessonStatus status);

    boolean existsByTeacherSkillIdAndStudentIdAndStatus(Long skillId, Long studentId, LessonStatus status);

    @Query("SELECT l " +
            "FROM Lesson l " +
            "WHERE (l.teacher.username = :username OR l.student.username = :username) AND " +
            "(l.startDateTime BETWEEN :lowerBound AND :upperBound) " +
            "AND l.status != ru.itis.project.dictionary.LessonStatus.PENDED " +
            "AND l.status != ru.itis.project.dictionary.LessonStatus.REJECTED " +
            "ORDER BY l.startDateTime ASC")
    List<Lesson> findAllByTeacherAndStudentUsernameNotPended(String username, LocalDateTime lowerBound, LocalDateTime upperBound);

    @Query("SELECT l " +
            "FROM Lesson l " +
            "WHERE (l.teacher.username = :username1 OR l.student.username = :username1 OR " +
            "l.teacher.username = :username2 OR l.student.username = :username2) AND " +
            "(l.startDateTime BETWEEN :lowerBound AND :upperBound) " +
            "AND l.status != ru.itis.project.dictionary.LessonStatus.PENDED " +
            "AND l.status != ru.itis.project.dictionary.LessonStatus.REJECTED " +
            "ORDER BY l.startDateTime ASC")
    List<Lesson> findAllByTeacherAndStudentUsernamesNotPendedAndNotRejected(String username1, String username2, LocalDateTime lowerBound, LocalDateTime upperBound);

}
