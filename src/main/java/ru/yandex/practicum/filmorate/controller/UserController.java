package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.dto.UserPatchDTO;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final InMemoryUserStorage users;
    private int seq = 0;

    @GetMapping
    public List<User> getAll() {
        return users.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDTO data) {
        log.info("handling POST /users");
        log.debug("with body={}", data);

        Integer id = getNextId();
        User user = data.toUser(id);

        log.info("creating user");
        log.debug("user={}", user);
        return users.create(id, user);
    }

    @PutMapping
    public User update(@Valid @RequestBody UserDTO.WithId data) {
        log.info("handling PUT /users");
        log.debug("with body={}", data);

        Integer id = data.getId();
        User user = data.toUser(id);

        log.info("updating user");
        log.debug("user={}", user);
        return users.update(id, user);
    }

    @PatchMapping("/{id}")
    public User updatePartially(@PathVariable Integer id,
                                @Valid @RequestBody UserPatchDTO data) {
        log.info("handling PATCH /users/{}", id);
        log.debug("with body={}", data);

        log.debug("getting saved user");
        Optional<User> savedUser = users.getById(id);

        if (savedUser.isEmpty()) {
            throw new NotFoundException("User with id=" + id + " not found");
        }

        User.UserBuilder builder = savedUser.get().toBuilder();

        if (data.getEmail() != null) {
            log.debug("updating user.email");
            builder.email(data.getEmail());
        }

        if (data.getLogin() != null) {
            log.debug("updating user.login");
            builder.login(data.getLogin());
        }

        if (data.getName() != null) {
            log.debug("updating user.name");
            builder.name(data.getName());
        }

        if (data.getBirthday() != null) {
            log.debug("updating user.birthday");
            builder.birthday(data.getBirthday());
        }

        User updatedUser = builder.build();

        log.debug("saving user");
        users.put(id, updatedUser);

        log.info("sending user");
        log.debug("user={}", updatedUser);
        return updatedUser;
    }

    private User putUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("name is blank, so login={} will be name", user.getName());
            user = user.toBuilder().name(user.getLogin()).build();
        }

        log.debug("saving user");
        users.put(user.getId(), user);

        log.info("sending user");
        log.trace("user={}", user);
        return user;
    }

    private Integer getNextId() {
        return ++seq;
    }
}
