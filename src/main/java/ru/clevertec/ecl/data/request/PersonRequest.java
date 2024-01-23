package ru.clevertec.ecl.data.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.clevertec.ecl.enums.Sex;

import java.util.List;
import java.util.UUID;

public record PersonRequest(

        UUID uuid,

        @NotBlank(message = "Person name is blank.")
        @Size(max = 30, message = "Person name is too long.")
        String name,

        @NotBlank(message = "Person surname is blank.")
        @Size(max = 30, message = "Person surname is too long.")
        String surname,

        Sex sex,

        @NotBlank(message = "Passport series is blank.")
        @Size(max = 4, message = "Passport series is too long.")
        String passportSeries,

        @NotBlank(message = "Passport number is blank.")
        @Size(max = 8, message = "Passport number is too long.")
        String passportNumber,

        UUID tenantHouseUuidRequest,

        List<UUID> ownerHousesUuidRequest
) {
}
