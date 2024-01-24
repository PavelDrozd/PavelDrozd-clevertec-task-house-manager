package ru.clevertec.ecl.data;

import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private UUID uuid = UUID.fromString("79e17dfd-a27d-45b6-8c72-a15538b8216e");

    @Builder.Default
    private String name = "Мария";

    @Builder.Default
    private String surname = "Лазарева";

    @Builder.Default
    private Sex sex = Sex.FEMALE;

    @Builder.Default
    private String passportSeries = "MP";

    @Builder.Default
    private String passportNumber = "7332632";

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.of(
            2024, 1, 1, 1, 1, 1, 111);

    @Builder.Default
    private LocalDateTime updateDate = LocalDateTime.of(
            2024, 2, 2, 2, 2, 2, 222);

    @Builder.Default
    private House residentHouse = HouseTestBuilder.builder().build().buildHouse();

    @Builder.Default
    private List<House> ownerHouses = List.of(HouseTestBuilder.builder().build().buildHouse());

    public Person buildPerson() {
        return new Person(id, uuid, name, surname, sex, passportSeries, passportNumber, createDate, updateDate,
                residentHouse, ownerHouses);
    }

    public PersonRequest buildPersonRequest() {
        return new PersonRequest(uuid, name, surname, sex, passportSeries, passportNumber,
                HouseTestBuilder.builder().build().buildHouseRequest().uuid(),
                List.of(HouseTestBuilder.builder().build().buildHouseRequest().uuid()));
    }

    public PersonResponse buildPersonResponse() {
        return new PersonResponse(uuid, name, surname, sex, passportSeries, passportNumber, createDate, updateDate,
                HouseTestBuilder.builder().build().buildHouseResponse(),
                List.of(HouseTestBuilder.builder().build().buildHouseResponse()));
    }

    public List<Person> buildPersonList() {
        return List.of(buildPerson());
    }

    public Page<Person> buildPersonPage() {
        return new PageImpl<>(buildPersonList());
    }

    public List<PersonRequest> buildPersonRequestList() {
        return List.of(buildPersonRequest());
    }

    public List<PersonResponse> buildPersonResponseList() {
        return List.of(buildPersonResponse());
    }

    public Page<PersonResponse> buildPersonResponsePage() {
        return new PageImpl<>(buildPersonResponseList());
    }

    public Person buildPersonForCreate() {
        return PersonTestBuilder.builder()
                .withId(null)
                .withUuid(null)
                .withName("Евгений")
                .withSurname("Борисов")
                .withSex(Sex.MALE)
                .withPassportSeries("MC")
                .withPassportNumber("1234567")
                .withCreateDate(null)
                .withUpdateDate(null)
                .withResidentHouse(HouseTestBuilder.builder().build().buildHouseForCreate())
                .withOwnerHouses(null)
                .build().buildPerson();
    }

    public Person buildPersonForUpdate() {
        return PersonTestBuilder.builder()
                .withId(10L)
                .withUuid(UUID.fromString("fd839347-a17c-44f0-a8b7-77b53d8a652d"))
                .withName("Полина")
                .withSurname("Леонова")
                .withSex(Sex.FEMALE)
                .withPassportSeries("MC")
                .withPassportNumber("1593875")
                .withCreateDate(LocalDateTime.of(
                        2023, 12, 28, 7, 12, 15, 156))
                .withUpdateDate(LocalDateTime.of(
                        2023, 12, 28, 7, 12, 15, 156))
                .withResidentHouse(residentHouse)
                .withOwnerHouses(List.of(residentHouse))
                .build().buildPerson();
    }

    public Person buildPersonForDelete() {
        return PersonTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("789c8c63-a58d-4e50-a4b4-33c15debfaf3"))
                .withName("Андрей")
                .withSurname("Румянцев")
                .withSex(Sex.MALE)
                .withPassportSeries("MP")
                .withPassportNumber("9474383")
                .withCreateDate(LocalDateTime.of(
                        2023, 9, 29, 6, 5, 15, 156))
                .withUpdateDate(LocalDateTime.of(
                        2023, 9, 29, 6, 5, 15, 156))
                .withResidentHouse(residentHouse)
                .withOwnerHouses(null)
                .build().buildPerson();
    }
}
