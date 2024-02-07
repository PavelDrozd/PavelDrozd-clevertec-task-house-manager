package ru.clevertec.exception;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.clevertec.data.Error;
import ru.clevertec.data.TestDataBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RestErrorHandlerTest {

    private final RestErrorHandler restErrorHandler = new RestErrorHandler();

    @Test
    void eternalServerErrorShouyldReturnExpectedErrorResponseEntity() {
        // given
        Exception exception = new Exception("Exception");
        ResponseEntity<Error> expected = TestDataBuilder.buildEternalServerErrorResponseEntityError();

        // when
        ResponseEntity<Error> actual = restErrorHandler.iternalServerError(exception);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("statusCode", expected.getStatusCode())
                .hasFieldOrPropertyWithValue("body", expected.getBody());
    }

    @Test
    void notFoundShouyldReturnExpectedErrorResponseEntity() {
        // given
        NotFoundException notFoundException = new NotFoundException("Not found exception");
        ResponseEntity<Error> expected = TestDataBuilder.buildNotFoundResponseEntityError();

        // when
        ResponseEntity<Error> actual = restErrorHandler.notFound(notFoundException);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("statusCode", expected.getStatusCode())
                .hasFieldOrPropertyWithValue("body", expected.getBody());
    }

    @Test
    void validationExceptionShouyldReturnExpectedErrorResponseEntity() {
        // given
        ValidationException validationException = new ValidationException("Validation exception");
        ResponseEntity<Error> expected = TestDataBuilder.buildValidationExceptionResponseEntityError();

        // when
        ResponseEntity<Error> actual = restErrorHandler.validationException(validationException);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("statusCode", expected.getStatusCode())
                .hasFieldOrPropertyWithValue("body", expected.getBody());
    }

    @SneakyThrows
    @Test
    void methodArgumentNotValidExceptionShouyldReturnExpectedErrorResponseEntity() {
        // given
        Object object = "Exception";
        Class<Object> objectClass = Object.class;
        BindingResult bindingResult = new BindException(object, object.toString());
        MethodParameter methodParameter = new MethodParameter(objectClass.getMethods()[0], 0);
        MethodArgumentNotValidException methodArgumentNotValidException =
                new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<Error> expected = TestDataBuilder.buildMethodArgumentNotValidExceptionResponseEntityError();

        // when
        ResponseEntity<Error> actual = restErrorHandler.methodArgumentNotValidException(methodArgumentNotValidException);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("statusCode", expected.getStatusCode())
                .hasFieldOrPropertyWithValue("body", expected.getBody());
    }
}