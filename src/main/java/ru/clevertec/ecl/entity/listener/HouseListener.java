package ru.clevertec.ecl.entity.listener;

import jakarta.persistence.PrePersist;
import ru.clevertec.ecl.entity.House;

import java.util.UUID;

public class HouseListener {

    @PrePersist
    public void persist(House house){
        UUID uuid = UUID.randomUUID();

        house.setUuid(uuid);
    }
}
