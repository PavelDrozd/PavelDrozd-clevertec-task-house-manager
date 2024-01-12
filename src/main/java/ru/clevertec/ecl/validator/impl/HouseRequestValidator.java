package ru.clevertec.ecl.validator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.exception.ValidationException;
import ru.clevertec.ecl.validator.ObjectValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class HouseRequestValidator implements ObjectValidator<HouseRequest> {

    private final Validator validator;

    @Override
    public void validate(HouseRequest houseRequest) {
        Set<ConstraintViolation<HouseRequest>> violations = validator.validate(houseRequest);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<HouseRequest> violation : violations) {
                throw ValidationException.of(HouseRequest.class, violation.getMessage());
            }
        }
    }
}
