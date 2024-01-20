package ru.clevertec.ecl.service;

import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;

import java.util.List;
import java.util.UUID;

public interface HouseService extends AbstractService<UUID, HouseRequest, HouseResponse> {

    List<PersonResponse> getPersonsByHouseUuid(UUID id);
}
