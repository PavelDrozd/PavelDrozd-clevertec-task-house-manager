package ru.clevertec.ecl.data.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record HouseRequest(

        UUID uuid,

        @NotBlank(message = "Country name is blank.")
        @NotEmpty(message = "Country name is empty.")
        @Size(max = 30, message = "Country name is too long.")
        String country,

        @NotBlank(message = "Area name is blank.")
        @NotEmpty(message = "Area name is empty.")
        @Size(max = 30, message = "Area name is too long.")
        String area,

        @NotBlank(message = "City name is blank.")
        @NotEmpty(message = "City name is empty.")
        @Size(max = 30, message = "City name is too long.")
        String city,

        @NotBlank(message = "Street name is blank.")
        @NotEmpty(message = "Street name is empty.")
        @Size(max = 50, message = "Street name is too long.")
        String street,

        @NotBlank(message = "Number is blank.")
        @NotEmpty(message = "Number is empty.")
        @Size(max = 10, message = "Number is too long.")
        String number,

        List<PersonRequest> residents
) {
}
