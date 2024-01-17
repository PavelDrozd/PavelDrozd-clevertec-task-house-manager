package ru.clevertec.ecl.validator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.exception.ValidationException;
import ru.clevertec.ecl.validator.ObjectValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PersonRequestValidator implements ObjectValidator<PersonRequest> {

    private final Validator validator;

    @Override
    public void validate(PersonRequest personRequest) {
        Set<ConstraintViolation<PersonRequest>> violations = validator.validate(personRequest);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<PersonRequest> violation : violations) {
                throw ValidationException.of(PersonRequest.class, violation.getMessage());
            }
        }
    }
}
