package ru.clevertec.ecl.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.ecl.config.TestSpringConfig;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.entity.Person;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
public class PersonRepositoryIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void findAllPaginationShouldReturnExpectedLimitOfHouses() {
        // given
        int limit = 3;
        int offset = 0;
        int expected = 3;

        // when
        List<Person> persons = personRepository.findAll(limit, offset);
        int actual = persons.size();

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
        Person actual = personRepository.findById(uuid).orElseThrow();

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
        UUID uuid = UUID.fromString("ac602dde-9556-4ef5-954c-aeebc42c5056");
        Person expected = PersonTestBuilder.builder().build().buildPerson();

        // when
        Person actual = personRepository.findPersonsByHouseUuid(uuid).get(0);

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
        Person actual = personRepository.create(person);

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
        Person actual = personRepository.update(person);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.passportSeries, expected.getPassportSeries())
                .hasFieldOrPropertyWithValue(Person.Fields.passportNumber, expected.getPassportNumber());
    }

    @Test
    public void deleteByIdShouldReturnExpectedUuid() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPersonForDelete().getUuid();
        Person expected = PersonTestBuilder.builder().build().buildPersonForDelete();

        // when
        personRepository.deleteById(uuid);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        Root<Person> personRoot = criteriaQuery.from(Person.class);

        criteriaQuery.select(personRoot).where(criteriaBuilder.equal(personRoot.get("deleted"), true));

        Person actual = entityManager.createQuery(criteriaQuery).getSingleResult();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid());
    }
}
