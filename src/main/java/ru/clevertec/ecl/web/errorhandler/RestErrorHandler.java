package ru.clevertec.ecl.web.errorhandler;

import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.data.error.ErrorDto;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.exception.ValidationException;

@Slf4j
@RestControllerAdvice("ru.clevertec.ecl.web.rest")
public class RestErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto error(Exception e) {
        log.error(e.getMessage(), e);
        return new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto notFound(NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ErrorDto(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto notFound(NoResultException e) {
        log.error(e.getMessage(), e);
        return new ErrorDto(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto validationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return new ErrorDto(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(javax.validation.ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto validationException(javax.validation.ValidationException e) {
        log.error(e.getMessage(), e);
        return new ErrorDto(HttpStatus.BAD_REQUEST);
    }
}
