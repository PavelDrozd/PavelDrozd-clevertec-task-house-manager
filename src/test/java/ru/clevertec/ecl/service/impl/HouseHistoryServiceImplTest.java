package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.data.HouseTestBuilder;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.enums.Type;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseHistoryRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseHistoryServiceImplTest {

    @Mock
    private HouseHistoryRepository houseHistoryRepository;

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

        List<Person> persons = PersonTestBuilder.builder().build().buildPersonList();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        List<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponseList();

        when(houseHistoryRepository.findPersonsByHouseUuidAndType(uuid, type)).thenReturn(persons);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponse);

        // when
        List<PersonResponse> actual = houseHistoryService.getTenantsByHouseUuid(uuid);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getOwnersByHouseUuid() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouse().getUuid();
        Type type = Type.OWNER;

        List<Person> persons = PersonTestBuilder.builder().build().buildPersonList();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();
        List<PersonResponse> expected = PersonTestBuilder.builder().build().buildPersonResponseList();

        when(houseHistoryRepository.findPersonsByHouseUuidAndType(uuid, type)).thenReturn(persons);
        when(personMapper.toPersonResponse(any())).thenReturn(personResponse);

        // when
        List<PersonResponse> actual = houseHistoryService.getOwnersByHouseUuid(uuid);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getHousesByTenantUuid() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        Type type = Type.TENANT;

        List<House> houses = HouseTestBuilder.builder().build().buildHouseList();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        List<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponseList();

        when(houseHistoryRepository.findHousesByPersonUuidAndType(uuid, type)).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        List<HouseResponse> actual = houseHistoryService.getHousesByTenantUuid(uuid);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getHousesByOwnerUuid() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        Type type = Type.OWNER;

        List<House> houses = HouseTestBuilder.builder().build().buildHouseList();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        List<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponseList();

        when(houseHistoryRepository.findHousesByPersonUuidAndType(uuid, type)).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        List<HouseResponse> actual = houseHistoryService.getHousesByOwnerUuid(uuid);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }
}