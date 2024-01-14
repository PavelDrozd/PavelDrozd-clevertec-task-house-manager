package ru.clevertec.ecl.data;

import lombok.Builder;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "with")
public class HouseTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private UUID uuid = UUID.fromString("6332f244-6747-4afd-823e-d1454010bf45");

    @Builder.Default
    private String area = "Минская область";

    @Builder.Default
    private String country = "Беларусь";

    @Builder.Default
    private String city = "Минск";

    @Builder.Default
    private String street = "проспект Независимости";

    @Builder.Default
    private String number = "101";

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.of(
            2024, 1, 1, 1, 1, 1, 111);

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private List<Person> residents = null;

    public House buildHouse() {
        return new House(id, uuid, area, country, city, street, number, createDate, deleted, null);
    }

    public HouseRequest buildHouseRequest() {
        return new HouseRequest(uuid, area, country, city, street, number, null);
    }

    public HouseResponse buildHouseResponse() {
        return new HouseResponse(uuid, area, country, city, street, number, createDate, null);
    }

    public List<House> buildHouseList() {
        return List.of(buildHouse());
    }

    public List<HouseRequest> buildHouseRequestList() {
        return List.of(buildHouseRequest());
    }

    public List<HouseResponse> buildHouseResponseList() {
        return List.of(buildHouseResponse());
    }
}
