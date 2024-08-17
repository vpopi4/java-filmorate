package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaces.Dao;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final Dao<MpaRating> dao;

    @GetMapping
    public List<MpaRating> getAll() {
        return dao.getAll();
    }

    @GetMapping("{id}")
    public MpaRating getById(@PathVariable Integer id) {
        return dao.getById(id).orElseThrow(NotFoundException::new);
    }
}
