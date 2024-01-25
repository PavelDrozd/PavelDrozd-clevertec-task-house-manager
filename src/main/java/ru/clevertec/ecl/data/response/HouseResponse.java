package ru.clevertec.ecl.data.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.ecl.constant.Constants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@FieldNameConstants
public record HouseResponse(

        UUID uuid,

        String country,

        String area,

        String city,

        String street,

        String number,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATA_FORMAT_ISO_8601)
        LocalDateTime createDate,

        @JsonIgnore
        List<PersonResponse> tenantResponse
) {
}
