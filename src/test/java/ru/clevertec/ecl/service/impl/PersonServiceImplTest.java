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
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.mapper.HouseMapper;
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

    @Mock
    private HouseMapper houseMapper;

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
        when(houseRepository.findByUuid(any())).thenReturn(Optional.ofNullable(person.getResidentHouse()));
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
    void getById() {
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
    void update() {
        // given
        PersonRequest personRequest = PersonTestBuilder.builder()
                .withPassportSeries("Mc")
                .build().buildPersonRequest();
        Person person = PersonTestBuilder.builder().build().buildPerson();
        House house = HouseTestBuilder.builder().build().buildHouse();
        Person expected = PersonTestBuilder.builder().build().buildPerson();

        when(personMapper.toPerson(personRequest)).thenReturn(person);
        when(personRepository.save(person)).thenReturn(person);
        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.of(person));
        when(houseRepository.findByUuid(house.getUuid())).thenReturn(Optional.of(house));

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
    void deleteById() {
        // given
        UUID uuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");

        // when
        personService.deleteById(uuid);

        // then
        verify(personRepository).deleteByUuid(uuid);
    }


    @Test
    void getHousesByPersonUuid() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        List<House> houses = HouseTestBuilder.builder().build().buildHouseList();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        List<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponseList();

        when(personRepository.findHousesByPersonUuid(uuid)).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        List<HouseResponse> actual = personService.getHousesByPersonUuid(uuid);

        //then
        assertThat(actual)
                .isEqualTo(expected);
    }
}