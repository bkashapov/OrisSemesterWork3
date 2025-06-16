package ru.itis.project.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itis.project.entity.Comment;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    int countAllBySkillId(Long skillId);
}
