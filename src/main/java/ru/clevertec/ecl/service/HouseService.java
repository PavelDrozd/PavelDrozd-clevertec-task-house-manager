package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;

import java.util.UUID;

public interface HouseService extends AbstractService<UUID, HouseRequest, HouseResponse> {

    Page<HouseResponse> getHousesByPersonUuid(UUID id, Pageable pageable);

    Page<HouseResponse> getByNameMatches(String name, Pageable pageable);
}
