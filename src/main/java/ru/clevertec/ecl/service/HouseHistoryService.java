package ru.clevertec.ecl.service;

import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;

import java.util.List;
import java.util.UUID;

public interface HouseHistoryService {

    List<PersonResponse> getTenantsByHouseUuid(UUID uuid);

    List<PersonResponse> getOwnersByHouseUuid(UUID uuid);

    List<HouseResponse> getHousesByTenantUuid(UUID uuid);

    List<HouseResponse> getHousesByOwnerUuid(UUID uuid);
}
