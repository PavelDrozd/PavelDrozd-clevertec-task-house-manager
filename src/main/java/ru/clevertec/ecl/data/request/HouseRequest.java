package ru.clevertec.ecl.data.request;


import jakarta.validation.constraints.Size;

import java.util.UUID;

public record HouseRequest(

        UUID uuid,

        @Size(max = 30, message = "Country name is too long.")
        String country,

        @Size(max = 30, message = "Area name is too long.")
        String area,

        @Size(max = 30, message = "City name is too long.")
        String city,

        @Size(max = 50, message = "Street name is too long.")
        String street,

        @Size(max = 10, message = "Number is too long.")
        String number
) {
}
