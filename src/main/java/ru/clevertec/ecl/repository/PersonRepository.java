package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    @Query(value = "SELECT p.ownerHouses " +
                   "FROM Person p " +
                   "WHERE deleted = false AND p.uuid = :uuid")
    List<House> findHousesByPersonUuid(UUID uuid);
}
