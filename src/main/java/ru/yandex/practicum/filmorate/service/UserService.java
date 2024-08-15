package ru.yandex.practicum.filmorate.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserDtoMapper;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.interfaces.UserDao;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserDao storage;
    private final FriendshipDao friendshipStorage;
    private final IdGenerator userIdGenerator;

    public List<User> getAll() throws DataAccessException {
        return storage.getAll();
    }

    public User getById(Integer id) throws NotFoundException, DataAccessException {
        return storage.getById(id)
                .orElseThrow(() -> new NotFoundException("User[id=" + id + "] not found"));
    }

    public User create(UserDTO.Request.Create dto) throws AlreadyExistException, DataAccessException {
        if (storage.getByEmail(dto.getEmail()).isPresent()
                || storage.getByLogin(dto.getLogin()).isPresent()) {
            throw new AlreadyExistException("User with such email or login already exist");
        }

        Supplier<User> createAndReturnUser = () -> {
            Integer id = userIdGenerator.getNextId();
            User user = UserDtoMapper.map(dto, id);

            storage.create(user);

            return getById(user.getId());
        };

        try {
            return createAndReturnUser.get();
        } catch (DuplicateKeyException e) {
            return createAndReturnUser.get(); // second attempt
        }
    }

    public User update(UserDTO.Request.Update dto)
            throws NotFoundException, AlreadyExistException, DataAccessException {
        if (storage.getByEmail(dto.getEmail()).isPresent()
                || storage.getByLogin(dto.getLogin()).isPresent()) {
            throw new AlreadyExistException("User with such email or login already exist");
        }

        User user = UserDtoMapper.map(dto);

        storage.update(user);

        return getById(user.getId());
    }

    public User updatePartially(
            Integer id,
            UserDTO.Request.UpdatePartially dto
    ) throws NotFoundException, DataAccessException {
        if (storage.getByEmail(dto.getEmail()).isPresent()
                || storage.getByLogin(dto.getLogin()).isPresent()) {
            throw new AlreadyExistException("User with such email or login already exist");
        }

        User.UserBuilder builder = getById(id).toBuilder();

        if (dto.getEmail() != null) {
            log.debug("updating user.email");

            if (storage.getByEmail(dto.getEmail()).isPresent()) {
                throw new AlreadyExistException("User with such email already exist");
            }

            builder.email(dto.getEmail());
        }

        if (dto.getLogin() != null) {
            log.debug("updating user.login");

            if (storage.getByLogin(dto.getLogin()).isPresent()) {
                throw new AlreadyExistException("User with such login already exist");
            }

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

        storage.update(user);

        return getById(user.getId());
    }

    public void delete(Integer id) {
        storage.delete(id);
    }

    public void createFriendship(Integer userId, Integer friendId) throws NotFoundException, AlreadyExistException {
        getById(userId);
        getById(friendId);

        Optional<Friendship> friendRequest = friendshipStorage.getByIds(userId, friendId);

        if (friendRequest.isPresent()) {
            throw new AlreadyExistException("friend request already sent");
        }

        Friendship.FriendshipBuilder requestBuilder = Friendship.builder()
                .userIdFrom(userId)
                .userIdTo(friendId);

        Optional<Friendship> friendRequestReverse = friendshipStorage.getByIds(friendId, userId);

        if (friendRequestReverse.isPresent()) {
            friendshipStorage.create(requestBuilder.isAccepted(true).build());
            friendshipStorage.update(friendRequestReverse.get().toBuilder().isAccepted(true).build());
        } else {
            friendshipStorage.create(requestBuilder.isAccepted(false).build());
        }
    }

    public void deleteFriendship(Integer userId, Integer friendId) throws NotFoundException {
        getById(userId);
        getById(friendId);

        friendshipStorage.delete(userId, friendId);

        Optional<Friendship> reverseRequest = friendshipStorage.getByIds(userId, friendId);

        if (reverseRequest.isPresent()) {
            friendshipStorage.update(reverseRequest.get().toBuilder().isAccepted(false).build());
        }
    }

    public Set<User> getFriends(Integer id) {
        getById(id);
        return friendshipStorage.getUserFriends(id);
    }

    public Set<User> getCommonFriends(Integer userId1, Integer userId2) throws NotFoundException {
        Set<User> user1Friends = getFriends(userId1);
        Set<User> user2Friends = getFriends(userId2);

        user1Friends.retainAll(user2Friends);

        return user1Friends;
    }
}
