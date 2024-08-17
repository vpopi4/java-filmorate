package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FriendshipRowMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");

        return Friendship.builder()
                .userIdFrom(rs.getInt("user_id_from"))
                .userIdTo(rs.getInt("user_id_to"))
                .isAccepted(rs.getBoolean("is_accepted"))
                .createdAt(createdAt != null
                        ? createdAt.toInstant()
                        : null)
                .build();
    }
}
