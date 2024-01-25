package ru.clevertec.ecl.integration.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.clevertec.ecl.config.DBContainerConfig;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.service.PersonService;
import ru.clevertec.ecl.util.DelayGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class PersonServiceImplIntegrationTest {

    private static final int THREAD_POOL = 6;

    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL);

    private final DelayGenerator delayGenerator = DelayGenerator.getRandomDelayGenerator(
            500, 2000, TimeUnit.MILLISECONDS);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse(DBContainerConfig.POSTGRES_CONTAINER_VERSION));

    @Autowired
    PersonService personService;

    @Test
    void shouldAssertAllHouseResponses() throws Exception {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        PersonRequest houseRequestForCreate = PersonTestBuilder.builder().build().buildPersonRequestForCreate();
        PersonRequest houseRequestForUpdate = PersonTestBuilder.builder().build().buildPersonRequestForUpdate();
        UUID uuidOfUpdatedHouse = houseRequestForUpdate.uuid();

        PersonResponse expectedGetPersonResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        PersonResponse expectedCreatePersonResponse = PersonTestBuilder.builder().build().buildPersonResponseForCreate();
        PersonResponse expectedUpdatePersonResponse = PersonTestBuilder.builder().build().buildPersonResponseForUpdate();

        AtomicReference<PersonResponse> getPersonResponse = new AtomicReference<>();
        AtomicReference<PersonResponse> createPersonResponse = new AtomicReference<>();
        AtomicReference<PersonResponse> updatePersonResponse = new AtomicReference<>();
        AtomicReference<PersonResponse> getUpdatedPersonResponse = new AtomicReference<>();

        // when
        List<Callable<Void>> tasks = new ArrayList<>();
        tasks.add(() -> {
            getPersonResponse.set(personService.getById(uuid));
            return null;
        });
        tasks.add(() -> {
            createPersonResponse.set(personService.create(houseRequestForCreate));
            return null;
        });
        tasks.add(() -> {
            updatePersonResponse.set(personService.update(houseRequestForUpdate));
            return null;
        });
        tasks.add(() -> {
            delayGenerator.delay();
            getUpdatedPersonResponse.set(personService.getById(uuidOfUpdatedHouse));
            return null;
        });
        tasks.add(() -> {
            delayGenerator.delay();
            personService.deleteById(uuid);
            return null;
        });

        List<Future<Void>> futures = executor.invokeAll(tasks);

        PersonResponse actualGetPersonResponse = getPersonResponse.get();
        PersonResponse actualCreatePersonResponse = createPersonResponse.get();
        PersonResponse actualUpdatePersonResponse = updatePersonResponse.get();
        PersonResponse actualGetUpdatedPersonResponse = getUpdatedPersonResponse.get();

        assertThat(actualGetPersonResponse)
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.uuid, expectedGetPersonResponse.uuid())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.name, expectedGetPersonResponse.name())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.surname, expectedGetPersonResponse.surname())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.sex, expectedGetPersonResponse.sex())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportSeries, expectedGetPersonResponse.passportSeries())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportNumber, expectedGetPersonResponse.passportNumber());

        assertThat(actualCreatePersonResponse)
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.name, expectedCreatePersonResponse.name())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.surname, expectedCreatePersonResponse.surname())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.sex, expectedCreatePersonResponse.sex())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportSeries, expectedCreatePersonResponse.passportSeries())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportNumber, expectedCreatePersonResponse.passportNumber());

        assertThat(actualUpdatePersonResponse)
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.uuid, expectedUpdatePersonResponse.uuid())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.name, expectedUpdatePersonResponse.name())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.surname, expectedUpdatePersonResponse.surname())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.sex, expectedUpdatePersonResponse.sex())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportSeries, expectedUpdatePersonResponse.passportSeries())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportNumber, expectedUpdatePersonResponse.passportNumber());

        assertThat(actualGetUpdatedPersonResponse)
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.uuid, expectedUpdatePersonResponse.uuid())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.name, expectedUpdatePersonResponse.name())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.surname, expectedUpdatePersonResponse.surname())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.sex, expectedUpdatePersonResponse.sex())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportSeries, expectedUpdatePersonResponse.passportSeries())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportNumber, expectedUpdatePersonResponse.passportNumber());
    }
}
