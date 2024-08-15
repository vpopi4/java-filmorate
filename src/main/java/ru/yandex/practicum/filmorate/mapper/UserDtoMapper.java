package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.NewUserDTO;
import ru.yandex.practicum.filmorate.model.User;

public class UserDtoMapper {
    public static User map(NewUserDTO.Request.Create dto, Integer id) {
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

    public static User map(NewUserDTO.Request.Update dto) {
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
