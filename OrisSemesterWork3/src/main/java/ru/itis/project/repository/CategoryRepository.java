package ru.itis.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.project.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
