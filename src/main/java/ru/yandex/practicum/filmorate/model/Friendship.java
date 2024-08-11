package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class Friendship {
    @NonNull Integer userIdFrom;
    @NonNull Integer userIdTo;
    Boolean isAccepted;
    Instant createdAt;
}
