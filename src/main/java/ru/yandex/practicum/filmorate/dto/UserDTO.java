package ru.yandex.practicum.filmorate.dto;

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
    }
}
