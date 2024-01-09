package ru.clevertec.ecl.data;

import java.time.LocalDateTime;
import java.util.UUID;

public record PersonDto(

        UUID uuid,

        String name,

        String surname,

        Sex sex,

        String passportSeries,

        String passportNumber,

        LocalDateTime createDate,

        LocalDateTime updateDate,

        HouseDto house
) {
    public enum Sex {
        MALE, FEMALE
    }
}
