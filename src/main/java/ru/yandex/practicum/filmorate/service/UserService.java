package ru.yandex.practicum.filmorate.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.dto.UserPatchDTO;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EntityStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;
import ru.yandex.practicum.filmorate.util.UserMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final EntityStorage<User> storage;
    private final IdGenerator idGenerator;

    public List<User> getAll() {
        return storage.getAll();
    }

    public User getById(Integer id) throws NotFoundException {
        return storage
                .getById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User[id=" + id + "] not found"
                ));
    }

    public User create(UserDTO dto) throws AlreadyExistException {
        Integer id = idGenerator.getNextId();
        HashSet<Integer> friends = new HashSet<>();
        User user = checkNameField(UserMapper.map(dto, id, friends));

        return storage.create(id, user);
    }

    public User update(UserDTO.WithId dto) throws NotFoundException {
        HashSet<Integer> friends = new HashSet<>();
        User user = checkNameField(UserMapper.map(dto, friends));

        return storage.update(user.getId(), user);
    }

    public User updatePartially(Integer id, UserPatchDTO dto) throws NotFoundException {
        User.UserBuilder builder = getById(id).toBuilder();

        if (dto.getEmail() != null) {
            log.debug("updating user.email");
            builder.email(dto.getEmail());
        }

        if (dto.getLogin() != null) {
            log.debug("updating user.login");
            builder.login(dto.getLogin());
        }

        if (StringUtils.isNotBlank(dto.getName())) {
            log.debug("updating user.name");
            builder.name(dto.getName());
        }

        if (dto.getBirthday() != null) {
            log.debug("updating user.birthday");
            builder.birthday(dto.getBirthday());
        }

        User user = builder.build();

        return storage.put(id, user);
    }

    public void delete(Integer id) {
        // TODO: check data safety (films, friends)
        storage.remove(id);
    }

    public void createFriendship(Integer userId, Integer friendId) throws NotFoundException {
        User user = getById(userId);
        User friend = getById(friendId);

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());

        storage.update(userId, user);
        storage.update(friendId, friend);
    }

    public void deleteFriendship(Integer userId, Integer friendId) throws NotFoundException {
        User user = getById(userId);
        User friend = getById(friendId);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());

        storage.update(userId, user);
        storage.update(friendId, friend);
    }

    public Set<User> getFriends(Integer id) {
        return getById(id)
                .getFriends()
                .stream()
                .map(this::getById)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(Integer userId1, Integer userId2) throws NotFoundException {
        Set<User> user1Friends = getFriends(userId1);
        Set<User> user2Friends = getFriends(userId2);

        user1Friends.retainAll(user2Friends);

        return user1Friends;
    }

    private User checkNameField(User user) {
        if (StringUtils.isBlank(user.getName())) {
            log.trace("name is blank, so login={} will be name", user.getName());
            return user
                    .toBuilder()
                    .name(user.getLogin())
                    .build();
        }
        return user;
    }
}
