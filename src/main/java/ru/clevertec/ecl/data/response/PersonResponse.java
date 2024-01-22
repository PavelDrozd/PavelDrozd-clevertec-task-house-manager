package ru.clevertec.ecl.data.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.ecl.constant.Constants;
import ru.clevertec.ecl.enums.Sex;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@FieldNameConstants
public record PersonResponse(

        UUID uuid,

        String name,

        String surname,

        Sex sex,

        String passportSeries,

        String passportNumber,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATA_FORMAT_ISO_8601)
        LocalDateTime createDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATA_FORMAT_ISO_8601)
        LocalDateTime updateDate,

        @JsonIgnore
        HouseResponse tenantHouseResponse,

        @JsonIgnore
        List<HouseResponse> ownerHousesResponse
) {
}
