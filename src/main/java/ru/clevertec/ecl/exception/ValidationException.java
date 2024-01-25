package ru.clevertec.ecl.exception;

public class ValidationException extends ApplicationException {

    public ValidationException(String message) {
        super(message);
    }

    public static ValidationException of(Class<?> clazz, Object object) {
        return new ValidationException("Object type of: " + clazz + " thrown by: " + object);
    }
}
