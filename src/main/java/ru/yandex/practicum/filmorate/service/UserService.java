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
import ru.yandex.practicum.filmorate.storage.JdbcFriendshipRepository;
import ru.yandex.practicum.filmorate.storage.UserDAO;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserDAO storage;
    private final JdbcFriendshipRepository friendshipStorage;
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

    public User create(NewUserDTO.Request.Create dto) throws AlreadyExistException {
        checkUnique(dto.getEmail(), dto.getLogin());

        User user = User.builder()
                .id(idGenerator.getNextId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName() != null
                        ? dto.getName()
                        : dto.getLogin())
                .birthday(dto.getBirthday())
                .build();

        try {
            return storage.create(user);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public User update(NewUserDTO.Request.Update dto) throws NotFoundException, AlreadyExistException, DataAccessException {
        checkUnique(dto.getEmail(), dto.getLogin());

        User user = User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName() != null
                        ? dto.getName()
                        : dto.getLogin())
                .birthday(dto.getBirthday())
                .build();

        try {
            return storage.update(user);
        } catch (NotFoundException e) {
            throw new NotFoundException("User[id=" + user.getId() + "] not found");
        }
    }

    private void checkUnique(String email, String login) {
        boolean isUnique = storage.checkUnique(email, login);

        if (!isUnique) {
            throw new AlreadyExistException(String.format(
                    "User[email='%s' | login='%s'] already exists",
                    email,
                    login
            ));
        }
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

        List<Friendship> friendships = friendshipStorage.getByIds(userId, friendId);

        if (friendships.size() > 2) {
            // TODO: log the list
            throw new IllegalStateException("more than 2 friend requests found");
        } else if (friendships.size() == 2) {
            throw new AlreadyExistException("friend request already sent");
        } else if (friendships.size() == 1) {
            Friendship friendship = friendships.getFirst();

            if (friendship.getUserIdFrom().equals(userId)) {
                throw new AlreadyExistException("friend request already sent");
            }

            friendshipStorage.create(Friendship.builder()
                    .userIdFrom(userId)
                    .userIdTo(friendId)
                    .isAccepted(true)
                    .build());
            friendshipStorage.update(friendship.toBuilder()
                    .isAccepted(true)
                    .build());
        } else {
            friendshipStorage.create(Friendship.builder()
                    .userIdFrom(userId)
                    .userIdTo(friendId)
                    .build());
        }
    }

    public void deleteFriendship(Integer userId, Integer friendId) throws NotFoundException {
        getById(userId);
        getById(friendId);

        friendshipStorage.delete(userId, friendId);

        List<Friendship> friendships = friendshipStorage.getByIds(userId, friendId);

        if (friendships.size() == 1) {
            friendshipStorage.update(friendships
                    .getFirst().toBuilder()
                    .isAccepted(false)
                    .build())
            ;
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
