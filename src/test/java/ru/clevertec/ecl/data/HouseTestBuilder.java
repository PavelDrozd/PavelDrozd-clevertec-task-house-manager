package ru.clevertec.ecl.data;

import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;

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

    public House buildHouse() {
        return new House(id, uuid, country, area, city, street, number, createDate, deleted, null);
    }

    public House buildHouseFirst() {
        return House.builder()
                .uuid(UUID.fromString("061783b1-c63b-4fb2-a9d0-9d90842911a2"))
                .country("Беларусь")
                .area("Минская область")
                .city("Солигорск")
                .street("К.Заслонова")
                .number("52")
                .build();
    }

    public House buildHouseSecond() {
        return House.builder()
                .uuid(UUID.fromString("a85a721b-c62b-428e-a777-3daa72fc5e3a"))
                .country("Беларусь")
                .area("Минская область")
                .city("Минск")
                .street("проспект Рокоссовского")
                .number("57")
                .build();
    }

    public HouseRequest buildHouseRequest() {
        return new HouseRequest(uuid, country, area, city, street, number);
    }

    public HouseResponse buildHouseResponse() {
        return new HouseResponse(uuid, country, area, city, street, number, createDate, null);
    }

    public HouseResponse buildHouseResponseFirst() {
        return new HouseResponse(UUID.fromString("061783b1-c63b-4fb2-a9d0-9d90842911a2"),
                "Беларусь",
                "Минская область",
                "Солигорск",
                "К.Заслонова",
                "52",
                null,
                null);
    }

    public HouseResponse buildHouseResponseSecond() {
        return new HouseResponse(UUID.fromString("a85a721b-c62b-428e-a777-3daa72fc5e3a"),
                "Беларусь",
                "Минская область",
                "Минск",
                "проспект Рокоссовского",
                "57",
                null,
                null);
    }

    public List<House> buildHouseList() {
        return List.of(buildHouse());
    }

    public Page<House> buildHousePage() {
        return new PageImpl<>(buildHouseList(), Pageable.ofSize(1), 1);
    }

    public List<HouseRequest> buildHouseRequestList() {
        return List.of(buildHouseRequest());
    }

    public List<HouseResponse> buildHouseResponseList() {
        return List.of(buildHouseResponse());
    }

    public Page<HouseResponse> buildHouseResponsePage() {
        return new PageImpl<>(buildHouseResponseList(), Pageable.ofSize(1), 1);
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

    public HouseRequest buildHouseRequestForCreate() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(null)
                .withCountry("Беларусь")
                .withArea("Гомельская область")
                .withCity("Гомель")
                .withStreet("Центральная")
                .withNumber("1")
                .withCreateDate(null)
                .build().buildHouseRequest();
    }

    public HouseRequest buildHouseRequestForUpdate() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("78cdcc8d-07df-496b-86aa-65aadd4cfc77"))
                .withCountry("Беларусь")
                .withArea("Могилевская область")
                .withCity("Могилев")
                .withStreet("Петрозаводская")
                .withNumber("3")
                .withCreateDate(null)
                .build().buildHouseRequest();
    }

    public HouseRequest buildHouseRequestForUpdatePart() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("78cdcc8d-07df-496b-86aa-65aadd4cfc77"))
                .withCountry(null)
                .withArea(null)
                .withCity("Могилев")
                .withStreet("Петрозаводская")
                .withNumber("3")
                .withCreateDate(null)
                .build().buildHouseRequest();
    }

    public HouseRequest buildHouseRequestForDelete() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("8ca3955d-b436-471d-872f-f2ce07ac3f15"))
                .withCountry("Беларусь")
                .withArea("Витебская область")
                .withCity("Витебск")
                .withStreet("Чкалова")
                .withNumber("1")
                .withCreateDate(null)
                .build().buildHouseRequest();
    }

    public HouseResponse buildHouseResponseForCreate() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(null)
                .withCountry("Беларусь")
                .withArea("Гомельская область")
                .withCity("Гомель")
                .withStreet("Центральная")
                .withNumber("1")
                .withCreateDate(null)
                .build().buildHouseResponse();
    }

    public HouseResponse buildHouseResponseForUpdate() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("78cdcc8d-07df-496b-86aa-65aadd4cfc77"))
                .withCountry("Беларусь")
                .withArea("Могилевская область")
                .withCity("Могилев")
                .withStreet("Петрозаводская")
                .withNumber("3")
                .withCreateDate(null)
                .build().buildHouseResponse();
    }

    public HouseResponse buildHouseResponseForUpdatePart() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("78cdcc8d-07df-496b-86aa-65aadd4cfc77"))
                .withCountry(country)
                .withArea(area)
                .withCity("Могилев")
                .withStreet("Петрозаводская")
                .withNumber("3")
                .withCreateDate(null)
                .build().buildHouseResponse();
    }

    public HouseResponse buildHouseResponseForDelete() {
        return HouseTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("8ca3955d-b436-471d-872f-f2ce07ac3f15"))
                .withCountry("Беларусь")
                .withArea("Витебская область")
                .withCity("Витебск")
                .withStreet("Чкалова")
                .withNumber("1")
                .withCreateDate(null)
                .build().buildHouseResponse();
    }
}
