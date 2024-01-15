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
    private UUID uuid = UUID.fromString("ac602dde-9556-4ef5-954c-aeebc42c5056");

    @Builder.Default
    private String area = "Минская область";

    @Builder.Default
    private String country = "Беларусь";

    @Builder.Default
    private String city = "Минск";

    @Builder.Default
    private String street = "Разинская";

    @Builder.Default
    private String number = "99";

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.of(
            2024, 1, 3, 9, 12, 15, 156);

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

    public House buildHouseForCreate() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(null)
                .withCountry("Беларусь")
                .withArea("Гомельская область")
                .withCity("Гомель")
                .withStreet("Центральная")
                .withNumber("1")
                .withCreateDate(null)
                .build().buildHouse();
    }

    public House buildHouseForUpdate() {
        return HouseTestBuilder.builder()
                .withId(10L)
                .withUuid(UUID.fromString("78cdcc8d-07df-496b-86aa-65aadd4cfc77"))
                .withCountry("Беларусь")
                .withArea("Могилевская область")
                .withCity("Могилев")
                .withStreet("Петрозаводская")
                .withNumber("3")
                .withCreateDate(LocalDateTime.of(
                        2023, 9, 29, 6, 5, 15, 156))
                .build().buildHouse();
    }

    public House buildHouseForDelete() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("8ca3955d-b436-471d-872f-f2ce07ac3f15"))
                .withCountry("Беларусь")
                .withArea("Витебская область")
                .withCity("Витебск")
                .withStreet("Чкалова")
                .withNumber("1")
                .withCreateDate(LocalDateTime.of(
                        2024, 1, 3, 9, 12, 15, 156))
                .build().buildHouse();
    }
}
