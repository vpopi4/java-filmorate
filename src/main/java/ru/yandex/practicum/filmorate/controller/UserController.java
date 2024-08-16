package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> getAll() {
        log.debug("handle GET /users");

        return service.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id) {
        log.debug("handle GET /users/{id}");
        log.debug("id={}", id);

        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody UserDTO.Request.Create dto) {
        log.debug("handle POST /users");
        log.debug("request body={}", dto);

        User user = service.create(dto);
        log.info("user was created: {}", user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody UserDTO.Request.Update dto) {
        log.debug("handle PUT /users");
        log.debug("request body={}", dto);

        User user = service.update(dto);
        log.info("user was updated: {}", user);

        return user;
    }

    @PatchMapping("/{id}")
    public User updatePartially(@PathVariable Integer id,
                                @Valid @RequestBody UserDTO.Request.UpdatePartially dto) {
        log.debug("handle PATCH /users");
        log.debug("id={}, request body={}", id, dto);

        User user = service.updatePartially(id, dto);
        log.info("user was updated: {}", user);

        return user;
    }

    @GetMapping("/{userId}/friends")
    public Set<User> getFriends(@PathVariable Integer userId) {
        log.debug("handle GET /users/{userId}/friends");
        log.debug("userId={}", userId);

        return service.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable Integer userId,
                                      @PathVariable Integer otherId) {
        log.debug("handle GET /users//{id}/friends/common/{otherId}");
        log.debug("userId={}", userId);

        return service.getCommonFriends(userId, otherId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void sendFriendRequest(@PathVariable Integer userId,
                                  @PathVariable Integer friendId) {
        log.debug("handle PUT /users/{userId}/friends/{friendId}");
        log.debug("userId={}, friendId={}", userId, friendId);

        service.sendFriendRequest(userId, friendId);
        log.debug("friend request was created");
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void revokeFriendRequest(@PathVariable Integer userId,
                                    @PathVariable Integer friendId) {
        log.debug("handle DELETE /users/{userId}/friends/{friendId}");
        log.debug("userId={}, friendId={}", userId, friendId);

        service.revokeFriendRequest(userId, friendId);
        log.debug("friend request was deleted");
    }
}
