package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.Dao;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final Dao<Genre> dao;

    @GetMapping
    public List<Genre> getAll() {
        return dao.getAll();
    }

    @GetMapping("{id}")
    public Genre getById(@PathVariable Integer id) {
        return dao.getById(id).orElseThrow(NotFoundException::new);
    }
}
