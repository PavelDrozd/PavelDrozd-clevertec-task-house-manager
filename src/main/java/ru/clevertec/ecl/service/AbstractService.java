package ru.clevertec.ecl.service;

import java.util.List;

public interface AbstractService<K, V, R> {

    R create(V v);

    List<R> getAll();

    List<R> getAll(int limit, int offset);

    R getById(K id);

    R update(V v);

    void deleteById(K id);

    int count();
}
