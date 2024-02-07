package ru.clevertec.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public static ValidationException of(Class<?> clazz, Object object) {
        return new ValidationException("Object type of: " + clazz + " thrown by: " + object);
    }
}
