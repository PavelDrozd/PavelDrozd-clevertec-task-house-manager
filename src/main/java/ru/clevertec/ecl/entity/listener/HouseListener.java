package ru.clevertec.ecl.entity.listener;

import jakarta.persistence.PrePersist;
import ru.clevertec.ecl.entity.House;

import java.time.LocalDateTime;
import java.util.UUID;

public class HouseListener {

    @PrePersist
    public void persist(House house) {
        LocalDateTime now = LocalDateTime.now();
        UUID uuid = UUID.randomUUID();

        house.setCreateDate(now);
        house.setUuid(uuid);
    }
}
