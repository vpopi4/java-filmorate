package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserRowMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class JdbcFriendshipStorage extends JdbcFriendshipDAO implements FriendshipDAO {
    public static final String GET_USER_FRIENDS_ID_QUERY = """
            SELECT user_id_to AS friend_id
            FROM friendships
            WHERE user_id_from = ? AND is_accepted = true
            UNION
            SELECT user_id_from AS friend_id
            FROM friendships
            WHERE user_id_to = ? AND is_accepted = true;
            """;

    @Autowired
    public JdbcFriendshipStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Set<Integer> getUserFriendsId(Integer userId) {
        return new HashSet<>(jdbcTemplate.queryForList(
                GET_USER_FRIENDS_ID_QUERY,
                Integer.class,
                userId, userId
        ));
    }

    public Set<User> getUserFriends(Integer userId) {
        String sql = "SELECT * FROM users WHERE id IN (%s)".formatted(GET_USER_FRIENDS_ID_QUERY);
        return new HashSet<>(jdbcTemplate.query(sql, new UserRowMapper(), userId, userId));
    }

    public List<Friendship> getByIds(Integer userId1, Integer userId2) {
        String sql = """
                SELECT * FROM friendships
                WHERE
                    (user_id_to = ? AND user_id_from = ?)
                OR
                    (user_id_to = ? AND user_id_from = ?);
                """;

        return jdbcTemplate.query(sql, getRowMapper(),
                userId1, userId2,
                userId2, userId1
        );
    }

    public void delete(Integer userId, Integer friendId) throws DataAccessException {
        String sql = "DELETE FROM friendships WHERE user_id_from = ? AND user_id_to = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }
}
