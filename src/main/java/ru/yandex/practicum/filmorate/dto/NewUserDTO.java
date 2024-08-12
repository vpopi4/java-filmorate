package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.validator.NotBlankIfNotNull;

import java.time.Instant;
import java.time.LocalDate;

public class NewUserDTO {
    private interface Id {
        Integer getId();
    }

    private interface Email {
        @jakarta.validation.constraints.Email
        String getEmail();
    }

    private interface Login {
        @Pattern(regexp = "\\S+", message = "must not contain spaces")
        String getLogin();
    }

    private interface Name {
        String getName();
    }

    private interface Birthday {
        @Past
        LocalDate getBirthday();
    }

    private interface CreatedAt {
        Instant getCreatedAt();
    }

    public static class Request {
        @Value
        @Builder
        public static class Create
                implements Email, Login, Name, Birthday {
            @NotNull String email;
            @NotBlank String login;
            String name;
            LocalDate birthday;
        }

        @Value
        @Builder
        public static class Update
                implements Id, Email, Login, Name, Birthday {
            @NotNull Integer id;
            @NotNull String email;
            @NotBlank String login;
            String name;
            LocalDate birthday;
        }

        @Value
        @Builder
        public static class UpdatePartially
                implements Email, Login, Name, Birthday {
            String email;
            @NotBlankIfNotNull
            String login;
            String name;
            LocalDate birthday;
        }
    }
}
