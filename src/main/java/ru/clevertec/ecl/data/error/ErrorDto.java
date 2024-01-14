package ru.clevertec.ecl.data.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorDto {

    private final String errorMessage;

    private final Integer errorCode;

    public ErrorDto(HttpStatus httpStatus) {
        errorMessage = httpStatus.getReasonPhrase();
        errorCode = httpStatus.value();
    }

    public ErrorDto(String errorMessage, Integer errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
