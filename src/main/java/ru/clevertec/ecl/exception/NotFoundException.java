package ru.clevertec.ecl.exception;

public class NotFoundException extends ApplicationException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public static NotFoundException of(Class<?> clazz, Object field) {
        return new NotFoundException("Object type of: " + clazz + " by field: " + field + " not found.");
    }
}
