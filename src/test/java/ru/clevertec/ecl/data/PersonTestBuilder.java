package ru.clevertec.ecl.data;

import lombok.Builder;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.Sex;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "with")
public class PersonTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private UUID uuid = UUID.fromString("b567881d-d421-4940-ab3f-6e57cabf56ab");

    @Builder.Default
    private String name = "Евгений";

    @Builder.Default
    private String surname = "Борисов";

    @Builder.Default
    private Sex sex = Sex.MALE;

    @Builder.Default
    private String passportSeries = "MC";

    @Builder.Default
    private String passportNumber = "2321843";

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.of(
            2024, 1, 1, 1, 1, 1, 111);

    @Builder.Default
    private LocalDateTime updateDate = LocalDateTime.of(
            2024, 2, 2, 2, 2, 2, 222);

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private House house = HouseTestBuilder.builder().build().buildHouse();

    @Builder.Default
    private List<House> houses = List.of(HouseTestBuilder.builder().build().buildHouse());

    public Person buildPerson() {
        return new Person(id, uuid, name, surname, sex, passportSeries, passportNumber, createDate, updateDate, deleted,
                house, houses);
    }

    public PersonRequest buildPersonRequest() {
        return new PersonRequest(uuid, name, surname, sex, passportSeries, passportNumber,
                HouseTestBuilder.builder().build().buildHouseRequest(),
                List.of(HouseTestBuilder.builder().build().buildHouseRequest()));
    }

    public PersonResponse buildPersonResponse() {
        return new PersonResponse(uuid, name, surname, sex, passportSeries, passportNumber, createDate, updateDate,
                HouseTestBuilder.builder().build().buildHouseResponse(),
                List.of(HouseTestBuilder.builder().build().buildHouseResponse()));
    }

    public List<Person> buildPersonList() {
        return List.of(buildPerson());
    }

    public List<PersonRequest> buildPersonRequestList() {
        return List.of(buildPersonRequest());
    }

    public List<PersonResponse> buildPersonResponseList() {
        return List.of(buildPersonResponse());
    }
}
