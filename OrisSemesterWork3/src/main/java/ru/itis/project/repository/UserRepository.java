package ru.itis.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.project.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
