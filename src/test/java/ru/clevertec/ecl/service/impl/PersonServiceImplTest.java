package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.HouseTestBuilder;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.repository.PersonRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private HouseMapper houseMapper;

    @InjectMocks
    private PersonServiceImpl personService;

    @Captor
    private ArgumentCaptor<Person> personCaptor;


    @Test
    void createShouldReturnExpectedPersonResponse() {
        // given
        PersonRequest personRequest = PersonTestBuilder.builder().build().buildPersonRequest();
        Person person = PersonTestBuilder.builder().build().buildPerson();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        PersonResponse expected = PersonTestBuilder.builder().build().buildPersonResponse();

        when(personMapper.toPerson(personRequest)).thenReturn(person);
        when(houseRepository.findByUuid(any())).thenReturn(Optional.ofNullable(person.getTenantHouse()));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toPersonResponse(person)).thenReturn(personResponse);

        // when
        PersonResponse actual = personService.create(personRequest);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.name, expected.name())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.surname, expected.surname())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.sex, expected.sex())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportSeries, expected.passportSeries())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportNumber, expected.passportNumber());
    }

    @Test
    void getAllShouldReturnSameElementsAsExpected() {
        // given
        int pageSize = 1;
        Page<Person> persons = PersonTestBuilder.builder().build().buildPersonPage();
        Pageable pageable = Pageable.ofSize(pageSize);
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        Page<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponsePage();

        when(personRepository.findAll(pageable)).thenReturn(persons);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponse);

        // when
        Page<PersonResponse> actual = personService.getAll(pageable);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getByIdShouldReturnExpectedPersonResponse() {
        // given
        Person person = PersonTestBuilder.builder().build().buildPerson();
        UUID uuid = person.getUuid();
        PersonResponse expected = PersonTestBuilder.builder().build().buildPersonResponse();

        when(personRepository.findByUuid(uuid)).thenReturn(Optional.of(person));
        when(personMapper.toPersonResponse(person)).thenReturn(expected);

        // when
        PersonResponse actual = personService.getById(uuid);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.uuid, expected.uuid())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.name, expected.name())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.surname, expected.surname())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.sex, expected.sex())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportSeries, expected.passportSeries())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportNumber, expected.passportNumber());
    }

    @Test
    void getByIdShouldThrowNotFoundExceptionWithExpectedMessage() {
        // given
        UUID fakeUuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f0");
        String expected = "not found";

        // when
        Exception exception = assertThrows(NotFoundException.class, () -> personService.getById(fakeUuid));
        String actual = exception.getMessage();

        // then
        assertThat(actual)
                .contains(expected);
    }

    @Test
    void updateShouldCaptorExpectedPerson() {
        // given
        PersonRequest personRequest = PersonTestBuilder.builder()
                .withPassportSeries("MC")
                .build().buildPersonRequest();
        PersonResponse personResponse = PersonTestBuilder.builder()
                .withPassportSeries("MC")
                .build().buildPersonResponse();
        Person person = PersonTestBuilder.builder().build().buildPerson();
        Person mergedPerson = PersonTestBuilder.builder()
                .withPassportSeries("MC")
                .build().buildPerson();
        House house = HouseTestBuilder.builder().build().buildHouse();
        Person expected = PersonTestBuilder.builder()
                .withPassportSeries("MC")
                .build().buildPerson();

        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.of(person));
        when(personMapper.mergeWithNulls(person, personRequest)).thenReturn(mergedPerson);
        when(personRepository.save(mergedPerson)).thenReturn(mergedPerson);
        when(houseRepository.findByUuid(any())).thenReturn(Optional.of(house));

        // when
        personService.update(personRequest);

        // then
        verify(personRepository)
                .save(personCaptor.capture());
        Person actual = personCaptor.getValue();

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.sex, expected.getSex())
                .hasFieldOrPropertyWithValue(Person.Fields.passportSeries, expected.getPassportSeries())
                .hasFieldOrPropertyWithValue(Person.Fields.passportNumber, expected.getPassportNumber())
                .hasFieldOrPropertyWithValue(Person.Fields.createDate, expected.getCreateDate());
    }

    @Test
    void updatePartShouldReturnExpectedPersonResponse() {
        // given
        PersonRequest personRequest = PersonTestBuilder.builder()
                .withName("Елизавета")
                .withSurname("Лисовская")
                .withSex(null)
                .withPassportSeries(null)
                .withPassportNumber(null)
                .withResidentHouse(null)
                .withOwnerHouses(null)
                .build().buildPersonRequest();
        Person person = PersonTestBuilder.builder().build().buildPerson();
        Person mergedPerson = PersonTestBuilder.builder()
                .withName("Елизавета")
                .withSurname("Лисовская")
                .build().buildPerson();
        PersonResponse personResponse = PersonTestBuilder.builder()
                .withName("Елизавета")
                .withSurname("Лисовская")
                .build().buildPersonResponse();
        PersonResponse expected = PersonTestBuilder.builder()
                .withName("Елизавета")
                .withSurname("Лисовская")
                .build().buildPersonResponse();
        House house = HouseTestBuilder.builder().build().buildHouse();

        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.of(person));
        when(personMapper.merge(person, personRequest)).thenReturn(mergedPerson);
        when(houseRepository.findByUuid(any())).thenReturn(Optional.ofNullable(house));
        when(personRepository.save(mergedPerson)).thenReturn(mergedPerson);
        when(personMapper.toPersonResponse(mergedPerson)).thenReturn(personResponse);

        // when
        PersonResponse actual = personService.updatePart(personRequest);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.uuid, expected.uuid())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.name, expected.name())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.surname, expected.surname())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.sex, expected.sex())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportSeries, expected.passportSeries())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.passportNumber, expected.passportNumber())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.createDate, expected.createDate())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.tenantHouseResponse, expected.tenantHouseResponse())
                .hasFieldOrPropertyWithValue(PersonResponse.Fields.ownerHousesResponse, expected.ownerHousesResponse());
    }

    @Test
    void deleteByIdShouldThrowNotFoundException() {
        // given
        UUID uuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");

        // when
        personService.deleteById(uuid);

        // then
        verify(personRepository).deleteByUuid(uuid);
    }


    @Test
    void getPersonsByHouseUuidShouldReturnExpectedPersonResponseList() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouse().getUuid();
        Pageable pageable = Pageable.unpaged();
        Page<Person> persons = PersonTestBuilder.builder().build().buildPersonPage();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        Page<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponsePage();

        when(personRepository.findByOwnerHouses_Uuid(uuid, pageable)).thenReturn(persons);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponse);

        // when
        Page<PersonResponse> actual = personService.getPersonsByHouseUuid(uuid, pageable);

        //then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getByNameMatchesShouldReturnExpectedHouseResponseList() {
        // given
        Pageable pageable = Pageable.unpaged();
        String name = PersonTestBuilder.builder().build().buildPerson().getPassportNumber();
        PersonResponse personResponseToReturn = PersonTestBuilder.builder().build().buildPersonResponse();
        Page<Person> personsToReturn = PersonTestBuilder.builder().build().buildPersonPage();
        Page<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponsePage();

        when(personRepository.findByNameMatches(name, pageable)).thenReturn(personsToReturn);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponseToReturn);

        // when
        Page<PersonResponse> actual = personService.getByNameMatches(name, pageable);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }
}