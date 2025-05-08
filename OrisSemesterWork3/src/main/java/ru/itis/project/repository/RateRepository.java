package ru.itis.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.project.entity.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
}
