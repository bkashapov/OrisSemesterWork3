package ru.itis.project.wrapper;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.itis.project.dto.CommentClientDto;

import java.util.List;

@Data
@Accessors(chain = true)
public class PageCommentDtoWrapper {
    private List<CommentClientDto> content;
    private int number;
    private int totalPages;
    private long totalElements;

    public Page<CommentClientDto> toPage() {
        return new PageImpl<>(content, PageRequest.of(number, 10), totalElements);
    }
}
