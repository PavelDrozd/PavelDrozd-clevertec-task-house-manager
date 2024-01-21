package ru.clevertec.ecl.data.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record HouseRequest(

        UUID uuid,

        @NotBlank(message = "Country name is blank.")
        @Size(max = 30, message = "Country name is too long.")
        String country,

        @NotBlank(message = "Area name is blank.")
        @Size(max = 30, message = "Area name is too long.")
        String area,

        @NotBlank(message = "City name is blank.")
        @Size(max = 30, message = "City name is too long.")
        String city,

        @NotBlank(message = "Street name is blank.")
        @Size(max = 50, message = "Street name is too long.")
        String street,

        @NotBlank(message = "Number is blank.")
        @Size(max = 10, message = "Number is too long.")
        String number
) {
}
