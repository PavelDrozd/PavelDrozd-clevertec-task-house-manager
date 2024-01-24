package ru.clevertec.ecl.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.clevertec.ecl.config.DBContainerConfig;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.Sex;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class PersonRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse(DBContainerConfig.POSTGRES_CONTAINER_VERSION));

    @Autowired
    private PersonRepository personRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void findAllPaginationShouldReturnExpectedLimitOfHouses() {
        // given
        int pageSize = 3;
        Pageable pageable = Pageable.ofSize(pageSize);
        int expected = 3;

        // when
        Page<Person> persons = personRepository.findAll(pageable);
        int actual = persons.getSize();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    public void findByIdShouldReturnExpectedHouse() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        Person expected = PersonTestBuilder.builder().build().buildPerson();

        // when
        Person actual = personRepository.findByUuid(uuid).orElseThrow();

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
    public void findPersonsByHouseUuidShouldReturnExpectedPerson() {
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
        Person actual = personRepository.findByOwnerHouses_Uuid(uuid, pageable)
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
    public void createShouldReturnExpectedHouse() {
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

    @Test
    public void updateShouldReturnExpectedPassportNumber() {
        // given
        Person person = PersonTestBuilder.builder()
                .withName("Алина")
                .withPassportSeries("MP")
                .withPassportNumber("7654321")
                .build().buildPersonForUpdate();
        Person expected = PersonTestBuilder.builder()
                .withName("Алина")
                .withPassportSeries("MP")
                .withPassportNumber("7654321")
                .build().buildPersonForUpdate();

        // when
        Person actual = personRepository.save(person);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.passportSeries, expected.getPassportSeries())
                .hasFieldOrPropertyWithValue(Person.Fields.passportNumber, expected.getPassportNumber());
    }
}
