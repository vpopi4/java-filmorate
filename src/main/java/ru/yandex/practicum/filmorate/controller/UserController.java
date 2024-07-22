package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserDTO;
import ru.yandex.practicum.filmorate.model.UserPatchDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users;
    private int seq;

    public UserController() {
        users = new HashMap<>();
    }

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDTO data) {
        log.info("handling POST /users");
        log.debug("with body={}", data);

        Integer id = getNextId();

        log.debug("creating user with id={}", id);
        User user = data.toUser(id);

        return putUser(user);
    }

    @PutMapping
    public User updateOrCreate(@Valid @RequestBody UserDTO.WithId data) {
        log.info("handling PUT /users");
        log.debug("with body={}", data);

        Integer id = data.getId();

        if (!users.containsKey(id)) {
            throw new NotFoundException("user with id=" + id + " not found");
        }

        log.debug("creating user with id={}", id);
        User user = data.toUser(id);

        return putUser(user);
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable Integer id,
                       @Valid @RequestBody UserPatchDTO data) {
        log.info("handling PATCH /users/{}", id);
        log.debug("with body={}", data);

        log.debug("getting saved user");
        User savedUser = users.get(id);

        if (savedUser == null) {
            throw new NotFoundException("User with id=" + id + " not found");
        }

        User.UserBuilder builder = savedUser.toBuilder();

        if (data.getEmail() != null) {
            log.trace("updating user.email");
            builder.email(data.getEmail());
        }

        if (data.getLogin() != null) {
            log.trace("updating user.login");
            builder.login(data.getLogin());
        }

        if (data.getName() != null) {
            log.trace("updating user.name");
            builder.name(data.getName());
        }

        if (data.getBirthday() != null) {
            log.trace("updating user.birthday");
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
