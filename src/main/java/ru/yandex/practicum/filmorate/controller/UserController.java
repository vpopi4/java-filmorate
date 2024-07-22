package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserDTO;
import ru.yandex.practicum.filmorate.model.UserPatchDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        return putUser(getNextId(), data);
    }

    @PutMapping("/{id}")
    public User updateOrCreate(@PathVariable Integer id,
                               @Valid @RequestBody UserDTO data) {
        Integer checkedId = users.containsKey(id) ? id : getNextId();

        return putUser(checkedId, data);
    }


    @PatchMapping("/{id}")
    public User update(@PathVariable Integer id,
                       @Valid @RequestBody UserPatchDTO data) {
        User savedUser = users.get(id);

        if (savedUser == null) {
            throw new NotFoundException("User with id=" + id + " not found");
        }

        User.UserBuilder builder = savedUser.toBuilder();

        if (data.getEmail() != null) {
            builder.email(data.getEmail());
        }

        if (data.getLogin() != null) {
            builder.login(data.getLogin());
        }

        if (data.getName() != null) {
            builder.name(data.getName());
        }

        if (data.getBirthday() != null) {
            builder.birthday(data.getBirthday());
        }

        User updatedUser = builder.build();

        users.put(id, updatedUser);

        return updatedUser;
    }

    private User putUser(Integer id, UserDTO data) {
        User user = data.toUser(id);

        if (data.getName().isBlank()) {
            user.toBuilder().name(user.getLogin());
        }

        users.put(user.getId(), user);

        return user;
    }

    private Integer getNextId() {
        return ++seq;
    }
}
