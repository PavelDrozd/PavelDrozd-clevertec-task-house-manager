package ru.clevertec.ecl.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.PostgresContainerInitializer;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.Sex;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryIntegrationTest extends PostgresContainerInitializer {

    private final PersonRepository personRepository;

    @Test
    public void findByDeletedFalseShouldReturnExpectedLimitOfHouses() {
        // given
        int pageSize = 3;
        Pageable pageable = Pageable.ofSize(pageSize);
        int expected = 3;

        // when
        Page<Person> persons = personRepository.findByDeletedFalse(pageable);
        int actual = persons.getSize();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    public void findByUuidAndDeletedFalseShouldReturnExpectedHouse() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        Person expected = PersonTestBuilder.builder().build().buildPerson();

        // when
        Person actual = personRepository.findByUuidAndDeletedFalse(uuid).orElseThrow();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.sex, expected.getSex())
                .hasFieldOrPropertyWithValue(Person.Fields.passportSeries, expected.getPassportSeries())
                .hasFieldOrPropertyWithValue(Person.Fields.passportNumber, expected.getPassportNumber());
    }


    @Test
    public void findByOwnerHouses_UuidAndDeletedFalseAndOwnerHouses_DeletedFalseShouldReturnExpectedPerson() {
        // given
        UUID uuid = UUID.fromString("63f0df3a-b447-4d76-ba8f-638b81a99b07");
        Pageable pageable = Pageable.unpaged();
        Person expected = PersonTestBuilder.builder()
                .withUuid(UUID.fromString("05242f3b-5e92-4ce4-a509-60c475dce50f"))
                .withName("Софья")
                .withSurname("Муравьева")
                .withSex(Sex.FEMALE)
                .withPassportSeries("MC")
                .withPassportNumber("9737347")
                .build().buildPerson();

        // when
        Person actual = personRepository.findByOwnerHouses_UuidAndDeletedFalseAndOwnerHouses_DeletedFalse(uuid, pageable)
                .getContent()
                .get(0);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.sex, expected.getSex())
                .hasFieldOrPropertyWithValue(Person.Fields.passportSeries, expected.getPassportSeries())
                .hasFieldOrPropertyWithValue(Person.Fields.passportNumber, expected.getPassportNumber());
    }


    @Test
    public void saveShouldReturnExpectedHouse() {
        // given
        Person person = PersonTestBuilder.builder().build().buildPersonForCreate();
        Person expected = PersonTestBuilder.builder().build().buildPersonForCreate();

        // when
        Person actual = personRepository.save(person);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.sex, expected.getSex())
                .hasFieldOrPropertyWithValue(Person.Fields.passportSeries, expected.getPassportSeries())
                .hasFieldOrPropertyWithValue(Person.Fields.passportNumber, expected.getPassportNumber());
    }
}
