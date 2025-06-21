package ru.itis.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.project.entity.Skill;

import java.util.List;

@Repository
public interface SkillRepository extends PagingAndSortingRepository<Skill, Long> {

    @Query("SELECT s FROM Skill s")
    List<Skill> getSkills(Pageable pageable);

    Skill findById(long id);

    void save(Skill skill);

    @Query("SELECT s FROM Skill s JOIN FETCH User u ON u = s.user WHERE u.username = :username")
    List<Skill> findAllByUsername(String username, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Skill s SET s.rating = :newRate, s.ratingCount = :newCount WHERE s.id = :id")
    void updateRatingAndRatingCount(Long id, Double newRate, int newCount);


    @Query("SELECT s FROM Skill s JOIN FETCH User u ON u = s.user WHERE LOWER(s.name) LIKE LOWER(:query) AND u.username = :username")
    Page<Skill> findAllByUsernameAndQuery(String username, String query, Pageable pageable);
}
