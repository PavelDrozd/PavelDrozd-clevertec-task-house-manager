package ru.clevertec.ecl.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class House {

    private UUID id;

    private UUID uuid;

    private String area;

    private String country;

    private String city;

    private String street;

    private String number;

    private LocalDateTime create_date;

    private List<Person> owners;
}
