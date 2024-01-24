package ru.clevertec.ecl.exception;

import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.data.error.ErrorDto;

@Slf4j
@RestControllerAdvice("ru.clevertec.ecl.controller")
public class RestErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDto> error(Exception e) {
        log.error(e.getMessage(), e);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(httpStatus)
                .body(new ErrorDto(httpStatus));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDto> notFound(NotFoundException e) {
        log.error(e.getMessage(), e);

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        return ResponseEntity.status(httpStatus)
                .body(new ErrorDto(httpStatus));
    }

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDto> notFound(NoResultException e) {
        log.error(e.getMessage(), e);

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        return ResponseEntity.status(httpStatus)
                .body(new ErrorDto(httpStatus));
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> validationException(ValidationException e) {
        log.error(e.getMessage(), e);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus)
                .body(new ErrorDto(httpStatus));
    }

    @ExceptionHandler(jakarta.validation.ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> validationException(jakarta.validation.ValidationException e) {
        log.error(e.getMessage(), e);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus)
                .body(new ErrorDto(httpStatus));
    }
}
