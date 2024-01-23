package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.HouseHistory;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.Type;

import java.util.List;
import java.util.UUID;

public interface HouseHistoryRepository extends JpaRepository<HouseHistory, Long> {

    @Query("SELECT hh.person " +
           "FROM HouseHistory hh " +
           "WHERE hh.house.uuid = :uuid " +
           "AND hh.type = :type")
    List<Person> findPersonsByHouseUuidAndType(UUID uuid, Type type);

    @Query("SELECT hh.house " +
           "FROM HouseHistory hh " +
           "WHERE hh.person.uuid = :uuid " +
           "AND hh.type = :type")
    List<House> findHousesByPersonUuidAndType(UUID uuid, Type type);
}
