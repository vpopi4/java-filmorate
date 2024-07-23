package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.validator.NotBlankIfNotNull;

import java.time.LocalDate;

@Value
@Builder
public class UserPatchDTO {
    @Email
    String email;

    @NotBlankIfNotNull
    @Pattern(regexp = "\\S+", message = "must not contain spaces")
    String login;

    String name;

    @Past
    LocalDate birthday;
}
