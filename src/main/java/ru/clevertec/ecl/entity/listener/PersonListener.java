package ru.clevertec.ecl.entity.listener;

import jakarta.persistence.PrePersist;
import ru.clevertec.ecl.entity.Person;

import java.time.LocalDateTime;
import java.util.UUID;

public class PersonListener {

    @PrePersist
    public void persist(Person person){
        LocalDateTime now = LocalDateTime.now();
        UUID uuid = UUID.randomUUID();

        person.setCreateDate(now);
        person.setUpdateDate(now);
        person.setUuid(uuid);
    }
}
