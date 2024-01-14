package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.entity.House;

import java.util.List;
import java.util.UUID;

public interface HouseRepository extends AbstractRepository<UUID, House> {

    List<House> findHousesByPersonUuid(UUID id);
}
