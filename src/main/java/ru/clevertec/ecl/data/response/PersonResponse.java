package ru.clevertec.ecl.data.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.clevertec.ecl.enums.Sex;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PersonResponse(

        UUID uuid,

        String name,

        String surname,

        Sex sex,

        String passportSeries,

        String passportNumber,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime createDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime updateDate,

        @JsonIgnore
        HouseResponse houseResponse,

        @JsonIgnore
        List<HouseResponse> houseResponses
) {
}
