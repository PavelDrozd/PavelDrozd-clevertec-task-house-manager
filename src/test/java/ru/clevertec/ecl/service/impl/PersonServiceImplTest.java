package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.data.HouseTestBuilder;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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

    @InjectMocks
    private PersonServiceImpl personService;

    @Captor
    private ArgumentCaptor<Person> personCaptor;


    @Test
    void create() {
        // given
        PersonRequest personRequest = PersonTestBuilder.builder().build().buildPersonRequest();
        Person person = PersonTestBuilder.builder().build().buildPerson();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        PersonResponse expected = PersonTestBuilder.builder().build().buildPersonResponse();

        when(personMapper.toPerson(personRequest)).thenReturn(person);
        when(houseRepository.findById(any())).thenReturn(Optional.ofNullable(person.getHouse()));
        when(personRepository.create(person)).thenReturn(person);
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
    void getAll() {
        // given
        List<Person> persons = PersonTestBuilder.builder().build().buildPersonList();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        List<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponseList();

        when(personRepository.findAll()).thenReturn(persons);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponse);

        // when
        List<PersonResponse> actual = personService.getAll();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void testGetAll() {
        // given
        int limit = 1;
        int offset = 0;
        List<Person> persons = PersonTestBuilder.builder().build().buildPersonList();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        List<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponseList();

        when(personRepository.findAll(limit, offset)).thenReturn(persons);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponse);

        // when
        List<PersonResponse> actual = personService.getAll(limit, offset);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getById() {
        // given
        Person person = PersonTestBuilder.builder().build().buildPerson();
        UUID uuid = person.getUuid();
        PersonResponse expected = PersonTestBuilder.builder().build().buildPersonResponse();

        when(personRepository.findById(uuid)).thenReturn(Optional.of(person));
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
    void update() {
        // given
        PersonRequest personRequest = PersonTestBuilder.builder()
                .withPassportSeries("MP")
                .build().buildPersonRequest();
        Person person = PersonTestBuilder.builder().build().buildPerson();
        House house = HouseTestBuilder.builder().build().buildHouse();
        Person expected = PersonTestBuilder.builder().build().buildPerson();

        when(personMapper.toPerson(personRequest)).thenReturn(person);
        when(personRepository.findById(person.getUuid())).thenReturn(Optional.of(person));
        when(houseRepository.findById(house.getUuid())).thenReturn(Optional.of(house));
        when(personRepository.update(person)).thenReturn(person);

        // when
        personService.update(personRequest);

        // then
        verify(personRepository)
                .update(personCaptor.capture());
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
    void deleteById() {
        // given
        UUID uuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");

        // when
        personService.deleteById(uuid);

        // then
        verify(personRepository).deleteById(uuid);
    }

    @Test
    void count() {
        // given
        Integer expected = 100;
        when(personRepository.count()).thenReturn(100);

        // when
        Integer actual = personRepository.count();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getPersonsByHouseUuid() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouse().getUuid();
        List<Person> persons = PersonTestBuilder.builder().build().buildPersonList();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        List<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponseList();

        when(personRepository.findPersonsByHouseUuid(uuid)).thenReturn(persons);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponse);

        // when
        List<PersonResponse> actual = personService.getPersonsByHouseUuid(uuid);

        //then
        assertThat(actual)
                .isEqualTo(expected);
    }
}