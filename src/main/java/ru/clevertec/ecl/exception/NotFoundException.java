package ru.clevertec.ecl.exception;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException of(Class<?> clazz, Object field) {
        return new NotFoundException("Object type of: " + clazz + " by field: " + field + " not found.");
    }
}
