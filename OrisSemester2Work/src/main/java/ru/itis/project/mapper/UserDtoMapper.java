package ru.itis.project.mapper;

import org.mapstruct.Mapper;
import ru.itis.project.dto.UserDto;
import ru.itis.project.entity.User;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    UserDto userToUserDto(User user);
}
