package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.entity.Person;

import java.util.List;
import java.util.UUID;

public interface PersonRepository extends AbstractRepository<UUID, Person> {

    List<Person> findPersonsByHouseUuid(UUID id);
}
