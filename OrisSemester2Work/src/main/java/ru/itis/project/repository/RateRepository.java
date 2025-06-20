package ru.itis.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.itis.project.entity.Rate;

import java.util.Optional;

@Repository
public interface RateRepository extends PagingAndSortingRepository<Rate, Long> {

    void save(Rate rate);

    boolean existsBySkillIdAndRaterUsername(Long skillId, String username);

    Page<Rate> findAllBySkillId(Long skillId, Pageable pageable);
}
