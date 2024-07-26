package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.dto.UserPatchDTO;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDTO data) {
        User user = service.create(data);
        log.info("user was created: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody UserDTO.WithId data) {
        User user = service.update(data);
        log.info("user was updated: {}", user);
        return user;
    }

    @PatchMapping("/{id}")
    public User updatePartially(@PathVariable Integer id,
                                @Valid @RequestBody UserPatchDTO data) {
        User user = service.updatePartially(id, data);
        log.info("user was updated: {}", user);
        return user;
    }
}
