package ru.clevertec.ecl.repository;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<K, T> {

    T create(T t);

    List<T> findAll();

    List<T> findAll(int limit, int offset);

    Optional<T> findById(K id);

    T update(T t);

    void deleteById(K id);

    long count();
}
