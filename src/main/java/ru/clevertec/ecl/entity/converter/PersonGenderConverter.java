package ru.clevertec.ecl.entity.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.enums.Gender;
import ru.clevertec.ecl.exception.ValidationException;

@Component
public class PersonGenderConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        return switch (gender) {
            case MALE -> 1;
            case FEMALE -> 2;
        };
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbData) {
        return switch (dbData) {
            case 1 -> Gender.MALE;
            case 2 -> Gender.FEMALE;
            default -> throw new ValidationException("Unexpected value: " + dbData);
        };
    }
}
