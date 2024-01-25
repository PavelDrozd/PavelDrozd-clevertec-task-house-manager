package ru.clevertec.ecl.data.request;


import ru.clevertec.ecl.enums.Sex;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record PersonRequest(

        UUID uuid,

        @NotBlank(message = "Person name is blank.")
        @NotEmpty(message = "Person name is empty.")
        @Size(max = 30, message = "Person name is too long.")
        String name,

        @NotBlank(message = "Person surname is blank.")
        @NotEmpty(message = "Person surname is empty.")
        @Size(max = 30, message = "Person surname is too long.")
        String surname,

        Sex sex,

        @NotBlank(message = "Passport series is blank.")
        @NotEmpty(message = "Passport series is empty.")
        @Size(max = 4, message = "Passport series is too long.")
        String passportSeries,

        @NotBlank(message = "Passport number is blank.")
        @NotEmpty(message = "Passport number is empty.")
        @Size(max = 8, message = "Passport number is too long.")
        String passportNumber,

        HouseRequest house,

        List<HouseRequest> houses
) {
}
