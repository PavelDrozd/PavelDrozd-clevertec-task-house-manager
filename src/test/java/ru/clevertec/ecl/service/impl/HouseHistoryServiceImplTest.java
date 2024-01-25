package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.HouseTestBuilder;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.Type;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.repository.PersonRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseHistoryServiceImplTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HouseMapper houseMapper;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private HouseHistoryServiceImpl houseHistoryService;

    @Test
    void getTenantsByHouseUuidShouldReturnExpectedListOfPersonsResponse() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouse().getUuid();
        Type type = Type.TENANT;
        Pageable pageable = Pageable.unpaged();

        Page<Person> persons = PersonTestBuilder.builder().build().buildPersonPage();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        Page<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponsePage();

        when(personRepository.findPersonsByHouseUuidAndType(uuid, type, pageable)).thenReturn(persons);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponse);

        // when
        Page<PersonResponse> actual = houseHistoryService.getTenantsByHouseUuid(uuid, pageable);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getOwnersByHouseUuid() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouse().getUuid();
        Type type = Type.OWNER;
        Pageable pageable = Pageable.unpaged();

        Page<Person> persons = PersonTestBuilder.builder().build().buildPersonPage();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        Page<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponsePage();

        when(personRepository.findPersonsByHouseUuidAndType(uuid, type, pageable)).thenReturn(persons);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponse);

        // when
        Page<PersonResponse> actual = houseHistoryService.getOwnersByHouseUuid(uuid, pageable);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getHousesByTenantUuid() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        Type type = Type.TENANT;
        Pageable pageable = Pageable.unpaged();

        Page<House> houses = HouseTestBuilder.builder().build().buildHousePage();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        Page<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponsePage();

        when(houseRepository.findHousesByPersonUuidAndType(uuid, type, pageable)).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        Page<HouseResponse> actual = houseHistoryService.getHousesByTenantUuid(uuid, pageable);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getHousesByOwnerUuid() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        Type type = Type.OWNER;
        Pageable pageable = Pageable.unpaged();

        Page<House> houses = HouseTestBuilder.builder().build().buildHousePage();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        Page<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponsePage();

        when(houseRepository.findHousesByPersonUuidAndType(uuid, type, pageable)).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        Page<HouseResponse> actual = houseHistoryService.getHousesByOwnerUuid(uuid, pageable);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }
}