package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;

import java.util.UUID;

public interface HouseHistoryService {

    Page<PersonResponse> getTenantsByHouseUuid(UUID uuid, Pageable pageable);

    Page<PersonResponse> getOwnersByHouseUuid(UUID uuid, Pageable pageable);

    Page<HouseResponse> getHousesByTenantUuid(UUID uuid, Pageable pageable);

    Page<HouseResponse> getHousesByOwnerUuid(UUID uuid, Pageable pageable);
}
