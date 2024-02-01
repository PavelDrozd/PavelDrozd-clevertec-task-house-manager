package ru.clevertec.ecl.data;

import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private boolean deleted = false;

    @Builder.Default
    private House residentHouse = HouseTestBuilder.builder().build().buildHouse();

    @Builder.Default
    private List<House> ownerHouses = List.of(HouseTestBuilder.builder().build().buildHouse());

    public Person buildPerson() {
        return new Person(id, uuid, name, surname, sex, passportSeries, passportNumber, createDate, updateDate, deleted,
                residentHouse, ownerHouses);
    }

    public Person buildPersonFirst() {
        return Person.builder()
                .uuid(UUID.fromString("03736b7f-3ca4-4af7-99ac-07628a7d8fe6"))
                .name("София")
                .surname("Макарова")
                .sex(Sex.FEMALE)
                .passportSeries("MC")
                .passportNumber("6373235")
                .tenantHouse(HouseTestBuilder.builder().build().buildHouseFirst())
                .build();
    }

    public Person buildPersonSecond() {
        return Person.builder()
                .uuid(UUID.fromString("12ff37bc-3fc8-47cd-8b18-d0b3fb35597b"))
                .name("Герман")
                .surname("Куликов")
                .sex(Sex.MALE)
                .passportSeries("MA")
                .passportNumber("2634736")
                .tenantHouse(HouseTestBuilder.builder().build().buildHouseSecond())
                .build();
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

    public PersonResponse buildPersonResponseFirst() {
        return new PersonResponse(UUID.fromString("03736b7f-3ca4-4af7-99ac-07628a7d8fe6"),
                "София",
                "Макарова",
                Sex.MALE,
                "MC",
                "6373235",
                null,
                null,
                HouseTestBuilder.builder().build().buildHouseResponseFirst(),
                null);
    }

    public PersonResponse buildPersonResponseSecond() {
        return new PersonResponse(UUID.fromString("12ff37bc-3fc8-47cd-8b18-d0b3fb35597b"),
                "Герман",
                "Куликов",
                Sex.MALE,
                "MA",
                "2634736",
                null,
                null,
                HouseTestBuilder.builder().build().buildHouseResponseSecond(),
                null);
    }

    public List<Person> buildPersonList() {
        return List.of(buildPerson());
    }

    public Page<Person> buildPersonPage() {
        return new PageImpl<>(buildPersonList(), Pageable.ofSize(1), 1);
    }

    public List<PersonRequest> buildPersonRequestList() {
        return List.of(buildPersonRequest());
    }

    public List<PersonResponse> buildPersonResponseList() {
        return List.of(buildPersonResponse());
    }

    public Page<PersonResponse> buildPersonResponsePage() {
        return new PageImpl<>(buildPersonResponseList(), Pageable.ofSize(1), 1);
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

    public PersonRequest buildPersonRequestForCreate() {
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
                .build().buildPersonRequest();
    }

    public PersonRequest buildPersonRequestForUpdate() {
        return PersonTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("fd839347-a17c-44f0-a8b7-77b53d8a652d"))
                .withName("Полина")
                .withSurname("Леонова")
                .withSex(Sex.FEMALE)
                .withPassportSeries("MC")
                .withPassportNumber("2223333")
                .withCreateDate(null)
                .withUpdateDate(null)
                .withResidentHouse(residentHouse)
                .withOwnerHouses(null)
                .build().buildPersonRequest();
    }

    public PersonRequest buildPersonRequestForUpdatePart() {
        return PersonTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("fd839347-a17c-44f0-a8b7-77b53d8a652d"))
                .withName("Полина")
                .withSurname("Леонова")
                .withSex(null)
                .withPassportSeries("MC")
                .withPassportNumber("2223333")
                .withCreateDate(null)
                .withUpdateDate(null)
                .withResidentHouse(null)
                .withOwnerHouses(null)
                .build().buildPersonRequest();
    }

    public PersonRequest buildPersonRequestForDelete() {
        return PersonTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("789c8c63-a58d-4e50-a4b4-33c15debfaf3"))
                .withName("Андрей")
                .withSurname("Румянцев")
                .withSex(Sex.MALE)
                .withPassportSeries("MP")
                .withPassportNumber("9474383")
                .withCreateDate(null)
                .withUpdateDate(null)
                .withResidentHouse(residentHouse)
                .withOwnerHouses(null)
                .build().buildPersonRequest();
    }

    public PersonResponse buildPersonResponseForCreate() {
        return PersonTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("d853f4c2-3889-4271-8053-57fec8c82958"))
                .withName("Евгений")
                .withSurname("Борисов")
                .withSex(Sex.MALE)
                .withPassportSeries("MC")
                .withPassportNumber("1234567")
                .withCreateDate(null)
                .withUpdateDate(null)
                .withResidentHouse(HouseTestBuilder.builder().build().buildHouseForCreate())
                .withOwnerHouses(null)
                .build().buildPersonResponse();
    }

    public PersonResponse buildPersonResponseForUpdate() {
        return PersonTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("fd839347-a17c-44f0-a8b7-77b53d8a652d"))
                .withName("Полина")
                .withSurname("Леонова")
                .withSex(Sex.FEMALE)
                .withPassportSeries("MC")
                .withPassportNumber("2223333")
                .withCreateDate(LocalDateTime.of(
                        2023, 12, 28, 7, 12, 15, 156))
                .withUpdateDate(LocalDateTime.of(
                        2023, 12, 28, 7, 12, 15, 156))
                .withResidentHouse(residentHouse)
                .withOwnerHouses(List.of(residentHouse))
                .build().buildPersonResponse();
    }

    public PersonResponse buildPersonResponseForUpdatePart() {
        return PersonTestBuilder.builder()
                .withId(null)
                .withUuid(UUID.fromString("fd839347-a17c-44f0-a8b7-77b53d8a652d"))
                .withName("Полина")
                .withSurname("Леонова")
                .withSex(Sex.FEMALE)
                .withPassportSeries("MC")
                .withPassportNumber("2223333")
                .withCreateDate(LocalDateTime.of(
                        2023, 12, 28, 7, 12, 15, 156))
                .withUpdateDate(LocalDateTime.of(
                        2023, 12, 28, 7, 12, 15, 156))
                .withResidentHouse(residentHouse)
                .withOwnerHouses(List.of(residentHouse))
                .build().buildPersonResponse();
    }

    public PersonResponse buildPersonResponseForDelete() {
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
                .build().buildPersonResponse();
    }
}
