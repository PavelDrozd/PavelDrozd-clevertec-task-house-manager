package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.enums.Type;

import java.util.Optional;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    Page<House> findByTenants_Uuid(UUID uuid, Pageable pageable);

    @Query("""
            SELECT h
            FROM House h
            WHERE h.area LIKE %:name%
            OR h.country LIKE %:name%
            OR h.city LIKE %:name%
            OR h.street LIKE %:name%
                    """)
    Page<House> findByNameMatches(String name, Pageable pageable);

    @Query("""
            SELECT hh.house
            FROM HouseHistory hh
            WHERE hh.person.uuid = :uuid
            AND hh.type = :type
            """)
    Page<House> findHousesByPersonUuidAndType(UUID uuid, Type type, Pageable pageable);
}
