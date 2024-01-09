package ru.clevertec.ecl.service;

import java.util.List;

public interface AbstractService<K, T> {

    K create(T t);

    List<T> getAll();

    T getById(K id);

    T update(K id, T t);

    void delete(K id);
}
