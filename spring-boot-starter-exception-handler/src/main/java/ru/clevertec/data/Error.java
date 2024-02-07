package ru.clevertec.data;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Error {

    private final String errorMessage;

    private final Integer errorCode;

    public Error(HttpStatus httpStatus) {
        errorMessage = httpStatus.getReasonPhrase();
        errorCode = httpStatus.value();
    }

    public Error(String errorMessage, Integer errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
