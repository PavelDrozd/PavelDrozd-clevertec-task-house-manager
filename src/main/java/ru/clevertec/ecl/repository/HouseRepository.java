package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    @Query(value = "SELECT h.residents " +
                   "FROM House h " +
                   "WHERE deleted = false AND h.uuid = :uuid")
    List<Person> findPersonsByHouseUuid(UUID uuid);
}
