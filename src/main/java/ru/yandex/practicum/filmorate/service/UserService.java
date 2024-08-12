package ru.yandex.practicum.filmorate.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserDTO;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.interfaces.UserDao;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserDao storage;
    private final FriendshipDao friendshipStorage;
    private final IdGenerator idGenerator;

    public List<User> getAll() throws DataAccessException {
        return storage.getAll();
    }

    public User getById(Integer id) throws NotFoundException, DataAccessException {
        return storage.getById(id)
                .orElseThrow(() -> new NotFoundException("User[id=" + id + "] not found"));
    }

    public User create(NewUserDTO.Request.Create dto) throws AlreadyExistException, DataAccessException {
        User user = User.builder()
                .id(idGenerator.getNextId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName() != null
                        ? dto.getName()
                        : dto.getLogin())
                .birthday(dto.getBirthday())
                .build();

        return storage.create(user);
    }

    public User update(NewUserDTO.Request.Update dto)
            throws NotFoundException, AlreadyExistException, DataAccessException {
        User user = User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName() != null
                        ? dto.getName()
                        : dto.getLogin())
                .birthday(dto.getBirthday())
                .build();

        return storage.update(user);
    }

    public User updatePartially(
            Integer id,
            NewUserDTO.Request.UpdatePartially dto
    ) throws NotFoundException, DataAccessException {
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

        return storage.update(user);
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

    public User findFriendsId(User user) {
        Set<Integer> friendsId = friendshipStorage.getUserFriendsId(user.getId());

        return user.toBuilder().friendsId(friendsId).build();
    }

    public Set<User> getFriends(Integer id) {
        return friendshipStorage.getUserFriends(id);
    }

    public Set<User> getCommonFriends(Integer userId1, Integer userId2) throws NotFoundException {
        Set<User> user1Friends = getFriends(userId1);
        Set<User> user2Friends = getFriends(userId2);

        user1Friends.retainAll(user2Friends);

        return user1Friends;
    }
}
