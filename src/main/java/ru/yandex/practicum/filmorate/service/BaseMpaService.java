package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseMpaService implements MpaService {
    private final MpaRepository mpaRepository;

    @Override
    public Mpa getMpa(int mpaId) {
        return mpaRepository.getMpa(mpaId);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaRepository.getAllMpa();
    }
}
