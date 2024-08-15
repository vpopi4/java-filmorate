package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;

public class UserDtoMapper {
    public static User map(UserDTO.Request.Create dto, Integer id) {
        return User.builder()
                .id(id)
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName() != null
                        ? dto.getName()
                        : dto.getLogin())
                .birthday(dto.getBirthday())
                .build();
    }

    public static User map(UserDTO.Request.Update dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName() != null
                        ? dto.getName()
                        : dto.getLogin())
                .birthday(dto.getBirthday())
                .build();
    }
}
