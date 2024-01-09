package ru.clevertec.ecl.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record HouseDto(

        UUID uuid,

        String area,

        String country,

        String city,

        String street,

        String number,

        LocalDateTime create_date,

        List<PersonDto> owners
) {
}
