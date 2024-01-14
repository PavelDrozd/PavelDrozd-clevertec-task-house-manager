package ru.clevertec.ecl.data.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime createDate,

        @JsonIgnore
        List<PersonResponse> residents
) {
}
