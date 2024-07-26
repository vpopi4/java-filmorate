package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public class UserMapper {
    public static User map(UserDTO dto, Integer id) {
        return User.builder()
                .id(id)
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName())
                .birthday(dto.getBirthday())
                .build();
    }

    public static User map(UserDTO.WithId dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName())
                .birthday(dto.getBirthday())
                .build();
    }
}
