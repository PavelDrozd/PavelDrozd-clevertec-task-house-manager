package ru.clevertec.ecl.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.enums.Type;
import ru.clevertec.ecl.exception.ValidationException;

@Component
@Converter
public class HouseHistoryTypeConverter implements AttributeConverter<Type, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Type type) {
        return switch (type) {
            case TENANT -> 1;
            case OWNER -> 2;
        };
    }

    @Override
    public Type convertToEntityAttribute(Integer dbData) {
        return switch (dbData) {
            case 1 -> Type.TENANT;
            case 2 -> Type.OWNER;
            default -> throw ValidationException.of(Type.class, dbData);
        };
    }
}
