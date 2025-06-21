package ru.itis.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.project.dto.CommentClientDto;
import ru.itis.project.dto.CommentClientFormDto;
import ru.itis.project.dto.CommentDto;
import ru.itis.project.dto.CommentFormDto;

@Mapper(componentModel = "spring")
public interface CommentDtoMapper {

    @Mapping(target = "authorId", source = "authorId")
    CommentClientFormDto toCommentClientFormDto(CommentFormDto dto, Long authorId);

    @Mapping(target = "authorUsername", source = "authorUsername")
    CommentDto toCommentDto(CommentClientDto dto, String authorUsername);
}
