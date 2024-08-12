package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendshipDao;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcFriendshipDao implements FriendshipDao {
    public static final String GET_USER_FRIENDS_ID_QUERY = """
            SELECT user_id_to AS friend_id
            FROM friendships
            WHERE user_id_from = ?
            """;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Friendship> mapper;
    private final RowMapper<User> userMapper;

    @Override
    public Friendship create(Friendship friendship) throws AlreadyExistException, DataAccessException {
        String sql = "INSERT INTO friendships (user_id_from, user_id_to, is_accepted) VALUES (?, ?, ?);";

        int rowsAffected = jdbcTemplate.update(sql,
                friendship.getUserIdFrom(),
                friendship.getUserIdTo(),
                friendship.getIsAccepted()
        );

        if (rowsAffected != 1) {
            throw new AlreadyExistException();
        }

        return friendship;
    }

    @Override
    public Friendship update(Friendship friendship) throws NotFoundException, DataAccessException {
        String sql = "UPDATE friendship SET is_accepted = ? WHERE user_id_from = ? AND user_id_to = ?;";

        int rowsAffected = jdbcTemplate.update(sql,
                friendship.getIsAccepted(),
                friendship.getUserIdFrom(),
                friendship.getUserIdTo()
        );

        if (rowsAffected != 1) {
            throw new NotFoundException();
        }

        return friendship;
    }

    @Override
    public Set<Integer> getUserFriendsId(Integer userId) {
        return new HashSet<>(jdbcTemplate.queryForList(
                GET_USER_FRIENDS_ID_QUERY,
                Integer.class,
                userId
        ));
    }

    @Override
    public Set<User> getUserFriends(Integer userId) throws DataAccessException {
        String sql = "SELECT * FROM users WHERE id IN (%s)".formatted(GET_USER_FRIENDS_ID_QUERY);
        return new HashSet<>(jdbcTemplate.query(sql, userMapper, userId));
    }

    @Override
    public List<Friendship> getManyByIds(Integer userId1, Integer userId2) throws DataAccessException {
        String sql = """
                SELECT * FROM friendships
                WHERE
                    (user_id_to = ? AND user_id_from = ?)
                OR
                    (user_id_to = ? AND user_id_from = ?);
                """;

        return jdbcTemplate.query(sql, mapper,
                userId1, userId2,
                userId2, userId1
        );
    }

    @Override
    public Optional<Friendship> getByIds(Integer userId, Integer friendId) throws DataAccessException {
        String sql = "SELECT * FROM friendships WHERE user_id_from = ? AND user_id_to = ?;";

        return jdbcTemplate.query(sql, mapper, userId, friendId).stream().findFirst();
    }

    @Override
    public void delete(Integer userId, Integer friendId) throws DataAccessException {
        String sql = "DELETE FROM friendships WHERE user_id_from = ? AND user_id_to = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }
}
