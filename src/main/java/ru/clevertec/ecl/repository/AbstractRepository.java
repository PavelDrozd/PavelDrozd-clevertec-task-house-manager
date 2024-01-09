package ru.clevertec.ecl.repository;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<K, T> {

    T create(T t);

    List<T> findAll();

    Optional<T> findById(K id);

    T update(T t);

    boolean deleteById(K id);
}
