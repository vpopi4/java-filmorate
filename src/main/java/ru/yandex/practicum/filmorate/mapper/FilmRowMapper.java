package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        long durationInMinutes = rs.getLong("duration_in_minutes");

        MpaRating mpaRating = MpaRating.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("rating_name"))
                .description(rs.getString("rating_description"))
                .build();

        return Film.builder()
                .id(id)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(Duration.ofMinutes(durationInMinutes))
                .mpa(mpaRating)
                .likesUserId(null)
                .genres(null)
                .build();
    }
}
