package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.model.User;

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

    @Value
    @Builder
    public static class WithId {
        @NotNull
        Integer id;

        @NotNull
        @Email
        String email;

        @NotBlank
        @Pattern(regexp = "\\S+", message = "must not contain spaces")
        String login;

        String name;

        @Past
        LocalDate birthday;

        public User toUser() {
            return User.builder()
                    .id(id)
                    .email(email)
                    .login(login)
                    .name(name)
                    .birthday(birthday)
                    .build();
        }

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
}
