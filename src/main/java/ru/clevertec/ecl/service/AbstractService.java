package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AbstractService<K, V, R> {

    R create(V v);

    Page<R> getAll(Pageable pageable);

    R getById(K id);

    R update(V v);

    R updatePart(V v);

    void deleteById(K id);
}
