package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserDTO {
    @NotNull
    @Email
    String email;

    @NotBlank
    @Pattern(regexp = "\\S+", message = "must not contain spaces")
    String login;

    String name;

    @Past
    LocalDate birthday;

    public User toUser(Integer id) {
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}
