package ru.clevertec.ecl.data.response;

import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public record PersonResponse(

        UUID uuid,

        String name,

        String surname,

        Sex sex,

        String passportSeries,

        String passportNumber,

        LocalDateTime createDate,

        LocalDateTime updateDate,

        House house
) {
}
