package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.Type;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    Page<Person> findByOwnerHouses_Uuid(UUID uuid, Pageable pageable);

    @Query("SELECT p " +
           "FROM Person p " +
           "WHERE p.name LIKE %:name% " +
           "OR p.surname LIKE %:name% " +
           "OR p.passportSeries LIKE %:name% " +
           "OR p.passportNumber LIKE %:name%")
    Page<Person> findByNameMatches(String name, Pageable pageable);

    @Query("""
            SELECT hh.person
            FROM HouseHistory hh
            WHERE hh.house.uuid = :uuid
            AND hh.type = :type
            """)
    Page<Person> findPersonsByHouseUuidAndType(UUID uuid, Type type, Pageable pageable);


}
