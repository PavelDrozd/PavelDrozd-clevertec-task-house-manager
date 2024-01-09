package ru.clevertec.ecl.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Person {

    private UUID id;

    private UUID uuid;

    private String name;

    private String surname;

    private Sex sex;

    private String passportSeries;

    private String passportNumber;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private House house;

    public enum Sex {
        MALE, FEMALE
    }
}
