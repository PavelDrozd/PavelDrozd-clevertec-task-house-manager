package ru.clevertec.ecl.data.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record HouseResponse(

        UUID uuid,

        String area,

        String country,

        String city,

        String street,

        String number,

        LocalDateTime createDate,

        List<PersonResponse> owners
) {
}
