package ru.clevertec.ecl.exception;

import java.util.Arrays;

public class ConfigurationException extends ApplicationException {

    public ConfigurationException(String message) {
        super(message);
    }

    public static ConfigurationException of(Class<?> clazz, Object... objects) {
        return new ConfigurationException(
                "Can't configure class: " + clazz + " set properties attributes: " + Arrays.toString(objects));
    }
}
