package ru.clevertec.ecl.data.request;


import jakarta.validation.constraints.Size;
import ru.clevertec.ecl.enums.Sex;

import java.util.List;
import java.util.UUID;

public record PersonRequest(

        UUID uuid,

        @Size(max = 30, message = "Person name is too long.")
        String name,

        @Size(max = 30, message = "Person surname is too long.")
        String surname,

        Sex sex,

        @Size(max = 4, message = "Passport series is too long.")
        String passportSeries,

        @Size(max = 8, message = "Passport number is too long.")
        String passportNumber,

        UUID tenantHouseUuidRequest,

        List<UUID> ownerHousesUuidRequest
) {
}
