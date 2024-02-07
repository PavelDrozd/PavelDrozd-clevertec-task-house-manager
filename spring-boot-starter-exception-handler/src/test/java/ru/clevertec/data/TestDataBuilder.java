package ru.clevertec.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TestDataBuilder {

    public static ResponseEntity<Error> buildEternalServerErrorResponseEntityError(){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(httpStatus)
                .body(new Error(httpStatus));
    }

    public static ResponseEntity<Error> buildNotFoundResponseEntityError(){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        return ResponseEntity.status(httpStatus)
                .body(new Error(httpStatus));
    }

    public static ResponseEntity<Error> buildValidationExceptionResponseEntityError(){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus)
                .body(new Error(httpStatus));
    }

    public static ResponseEntity<Error> buildMethodArgumentNotValidExceptionResponseEntityError(){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus)
                .body(new Error(httpStatus));
    }
}
