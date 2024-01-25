package ru.clevertec.ecl.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.enums.Sex;
import ru.clevertec.ecl.exception.ValidationException;

@Component
@Converter
public class PersonSexConverter implements AttributeConverter<Sex, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Sex sex) {
        return switch (sex) {
            case MALE -> 1;
            case FEMALE -> 2;
        };
    }

    @Override
    public Sex convertToEntityAttribute(Integer dbData) {
        return switch (dbData) {
            case 1 -> Sex.MALE;
            case 2 -> Sex.FEMALE;
            default -> throw ValidationException.of(Sex.class, dbData);
        };
    }
}
