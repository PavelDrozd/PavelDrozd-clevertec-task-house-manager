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
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseMapper houseMapper;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private HouseServiceImpl houseService;

    @Captor
    private ArgumentCaptor<House> houseCaptor;

    @Test
    void createShouldReturnExpectedHouseResponse() {
        // given
        HouseRequest houseRequest = HouseTestBuilder.builder().build().buildHouseRequest();
        House house = HouseTestBuilder.builder().build().buildHouse();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        HouseResponse expected = HouseTestBuilder.builder().build().buildHouseResponse();

        when(houseMapper.toHouse(houseRequest)).thenReturn(house);
        when(houseRepository.save(house)).thenReturn(house);
        when(houseMapper.toHouseResponse(house)).thenReturn(houseResponse);

        // when
        HouseResponse actual = houseService.create(houseRequest);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.country, expected.country())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.area, expected.area())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.city, expected.city())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.street, expected.street())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.number, expected.number());
    }

    @Test
    void getAllShouldReturnSameElementsAsExpected() {
        // given
        int pageSize = 1;
        Page<House> houses = HouseTestBuilder.builder().build().buildHousePage();
        Pageable pageable = Pageable.ofSize(pageSize);
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        Page<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponsePage();

        when(houseRepository.findAll(Pageable.unpaged())).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        Page<HouseResponse> actual = houseService.getAll(Pageable.unpaged());

        // then
        assertThat(actual)
                .hasSameElementsAs(expected);
    }

    @Test
    void getByIdShouldReturnExpectedHouseResponse() {
        // given
        House house = HouseTestBuilder.builder().build().buildHouse();
        UUID uuid = house.getUuid();
        HouseResponse expected = HouseTestBuilder.builder().build().buildHouseResponse();

        when(houseRepository.findByUuid(uuid)).thenReturn(Optional.of(house));
        when(houseMapper.toHouseResponse(house)).thenReturn(expected);

        // when
        HouseResponse actual = houseService.getById(uuid);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.uuid, expected.uuid())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.country, expected.country())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.area, expected.area())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.city, expected.city())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.street, expected.street())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.number, expected.number());
    }

    @Test
    void getByIdShouldThrowNotFoundExceptionWithExpectedMessage() {
        // given
        UUID fakeUuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f0");
        String expected = "not found";

        // when
        Exception exception = assertThrows(NotFoundException.class, () -> houseService.getById(fakeUuid));
        String actual = exception.getMessage();

        // then
        assertThat(actual)
                .contains(expected);
    }

    @Test
    void updateShouldCaptorExpectedHouse() {
        // given
        HouseRequest houseRequest = HouseTestBuilder.builder()
                .withNumber("25")
                .build().buildHouseRequest();
        House house = HouseTestBuilder.builder().build().buildHouse();
        House mergedHouse = HouseTestBuilder.builder()
                .withNumber("25")
                .build().buildHouse();
        House expected = HouseTestBuilder.builder()
                .withNumber("25")
                .build().buildHouse();

        when(houseRepository.findByUuid(house.getUuid())).thenReturn(Optional.of(house));
        when(houseMapper.mergeWithNulls(house, houseRequest)).thenReturn(mergedHouse);
        when(houseRepository.save(mergedHouse)).thenReturn(mergedHouse);

        // when
        houseService.update(houseRequest);

        // then
        verify(houseRepository)
                .save(houseCaptor.capture());
        House actual = houseCaptor.getValue();

        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber())
                .hasFieldOrPropertyWithValue(House.Fields.createDate, expected.getCreateDate());
    }

    @Test
    void updatePartShouldReturnExpectedHouseResponse() {
        // given
        HouseRequest houseRequest = HouseTestBuilder.builder()
                .withCountry(null)
                .withArea(null)
                .withCity(null)
                .withStreet("Пролетарская")
                .withNumber("1")
                .build().buildHouseRequest();
        House house = HouseTestBuilder.builder().build().buildHouse();
        House mergedHouse = HouseTestBuilder.builder()
                .withStreet("Пролетарская")
                .withNumber("1")
                .build().buildHouse();
        HouseResponse houseResponse = HouseTestBuilder.builder()
                .withStreet("Пролетарская")
                .withNumber("1")
                .build().buildHouseResponse();
        HouseResponse expected = HouseTestBuilder.builder()
                .withStreet("Пролетарская")
                .withNumber("1")
                .build().buildHouseResponse();


        when(houseRepository.findByUuid(house.getUuid())).thenReturn(Optional.of(house));
        when(houseMapper.merge(house, houseRequest)).thenReturn(mergedHouse);
        when(houseRepository.save(mergedHouse)).thenReturn(mergedHouse);
        when(houseMapper.toHouseResponse(mergedHouse)).thenReturn(houseResponse);

        // when
        HouseResponse actual = houseService.updatePart(houseRequest);

        // then

        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.uuid())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.country())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.area())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.city())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.street())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.number())
                .hasFieldOrPropertyWithValue(House.Fields.createDate, expected.createDate());
    }

    @Test
    void deleteByIdShouldThrowNotFoundException() {
        // given
        UUID uuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");

        // when
        houseService.deleteById(uuid);

        // then
        verify(houseRepository).deleteByUuid(uuid);
    }

    @Test
    void getHousesByPersonUuidShouldReturnExpectedHouseResponseList() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        Pageable pageable = Pageable.unpaged();
        Page<House> houses = HouseTestBuilder.builder().build().buildHousePage();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        Page<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponsePage();

        when(houseRepository.findByTenants_Uuid(uuid, pageable)).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        Page<HouseResponse> actual = houseService.getHousesByPersonUuid(uuid, pageable);

        //then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getByNameMatchesShouldReturnExpectedHouseResponseList() {
        // given
        Pageable pageable = Pageable.unpaged();
        String name = HouseTestBuilder.builder().build().buildHouse().getStreet();
        HouseResponse houseResponseToReturn = HouseTestBuilder.builder().build().buildHouseResponse();
        Page<House> housesToReturn = HouseTestBuilder.builder().build().buildHousePage();
        Page<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponsePage();

        when(houseRepository.findByNameMatches(name, pageable)).thenReturn(housesToReturn);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponseToReturn);

        // when
        Page<HouseResponse> actual = houseService.getByNameMatches(name, pageable);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }
}