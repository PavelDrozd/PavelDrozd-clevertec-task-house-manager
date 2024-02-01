package ru.clevertec.ecl.integration.service.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.repository.PersonRepository;
import ru.clevertec.ecl.service.impl.PersonServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PersonServiceImplCacheIntegrationTest {

    private static final int THREAD_POOL = 6;

    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL);

    private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    @Autowired
    private PersonServiceImpl personService;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private HouseRepository houseRepository;

    @MockBean
    private PersonMapper personMapper;

    @SneakyThrows
    @Test
    void shouldThrowFindByUuidAndDeletedFalse2Times() {
        Person personFirst = PersonTestBuilder.builder().build().buildPersonFirst();
        Person personSecond = PersonTestBuilder.builder().build().buildPersonSecond();

        UUID uuidFirst = personFirst.getUuid();
        UUID uuidSecond = personSecond.getUuid();

        PersonResponse personResponseFirst = PersonTestBuilder.builder().build().buildPersonResponseFirst();
        PersonResponse personResponseSecond = PersonTestBuilder.builder().build().buildPersonResponseSecond();

        when(personRepository.findByUuidAndDeletedFalse(uuidFirst)).thenReturn(Optional.of(personFirst));
        when(personRepository.findByUuidAndDeletedFalse(uuidSecond)).thenReturn(Optional.of(personSecond));
        when(personMapper.toPersonResponse(personFirst)).thenReturn(personResponseFirst);
        when(personMapper.toPersonResponse(personSecond)).thenReturn(personResponseSecond);

        List<Callable<Void>> tasks = new ArrayList<>();
        tasks.add(() -> {
            personService.getById(uuidFirst);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(1000);
            personService.getById(uuidSecond);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(2000);
            personService.getById(uuidFirst);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(3000);
            personService.getById(uuidSecond);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(4000);
            personService.getById(uuidFirst);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(5000);
            personService.getById(uuidSecond);
            return null;
        });

        executor.invokeAll(tasks);

        verify(personRepository, times(2)).findByUuidAndDeletedFalse(any(UUID.class));
    }

    @SneakyThrows
    @Test
    void shouldAssertAllHouseResponses() {
        // given
        Person personForGet = PersonTestBuilder.builder().build().buildPerson();
        Person personForCreate = PersonTestBuilder.builder().build().buildPersonForCreate();
        Person personForUpdate = PersonTestBuilder.builder().build().buildPersonForUpdate();
        Person personForDelete = PersonTestBuilder.builder().build().buildPersonForDelete();
        Person personWithDeletedTrue = PersonTestBuilder.builder()
                .withDeleted(true)
                .build().buildPersonForDelete();

        UUID uuidForGet = personForGet.getUuid();
        UUID uuidForUpdate = personForUpdate.getUuid();
        UUID uuidForDelete = personForDelete.getUuid();
        PersonRequest personRequestForCreate = PersonTestBuilder.builder().build().buildPersonRequestForCreate();
        PersonRequest personRequestForUpdate = PersonTestBuilder.builder().build().buildPersonRequestForUpdate();

        PersonResponse personResponseGet = PersonTestBuilder.builder().build().buildPersonResponse();
        PersonResponse personResponseCreate = PersonTestBuilder.builder().build().buildPersonResponseForCreate();
        PersonResponse personResponseUpdate = PersonTestBuilder.builder().build().buildPersonResponseForUpdate();

        PersonResponse expectedGetPersonResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        PersonResponse expectedCreatePersonResponse = PersonTestBuilder.builder().build().buildPersonResponseForCreate();
        PersonResponse expectedUpdatePersonResponse = PersonTestBuilder.builder().build().buildPersonResponseForUpdate();

        when(personRepository.findByUuidAndDeletedFalse(uuidForGet))
                .thenReturn(Optional.of(personForGet));

        when(personRepository.save(personForCreate))
                .thenReturn(personForCreate);
        when(houseRepository.findByUuidAndDeletedFalse(any()))
                .thenReturn(Optional.ofNullable(personForCreate.getTenantHouse()));

        when(personRepository.findByUuidAndDeletedFalse(uuidForUpdate))
                .thenReturn(Optional.of(personForUpdate));
        when(personRepository.save(personForUpdate))
                .thenReturn(personForUpdate);

        when(personRepository.findByUuidAndDeletedFalse(uuidForDelete))
                .thenReturn(Optional.of(personForDelete));
        when(personRepository.save(personWithDeletedTrue))
                .thenReturn(personWithDeletedTrue);

        when(personMapper.toPerson(personRequestForCreate))
                .thenReturn(personForCreate);
        when(personMapper.toPerson(personRequestForUpdate))
                .thenReturn(personForUpdate);

        when(personMapper.toPersonResponse(personForGet))
                .thenReturn(personResponseGet);
        when(personMapper.toPersonResponse(personForCreate))
                .thenReturn(personResponseCreate);
        when(personMapper.toPersonResponse(personForUpdate))
                .thenReturn(personResponseUpdate);

        when(personMapper.mergeWithNulls(personForUpdate, personRequestForUpdate))
                .thenReturn(personForUpdate);

        // when
        Future<PersonResponse> getPersonResponse = executor.submit(() -> personService.getById(uuidForGet));
        Future<PersonResponse> createPersonResponse = executor.submit(() -> personService.create(personRequestForCreate));
        Future<PersonResponse> updatePersonResponse = executor.submit(() -> personService.update(personRequestForUpdate));
        executor.submit(() -> personService.deleteById(uuidForDelete));

        PersonResponse actualGetPersonResponse = getPersonResponse.get();
        PersonResponse actualCreatePersonResponse = createPersonResponse.get();
        PersonResponse actualUpdatePersonResponse = updatePersonResponse.get();

        // then
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
    }
}
