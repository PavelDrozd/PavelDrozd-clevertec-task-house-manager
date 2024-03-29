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
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.repository.HouseRepository;

import java.util.List;
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
        when(houseRepository.create(house)).thenReturn(house);
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
    void getAllShouldReturnExpectedList() {
        // given
        List<House> houses = HouseTestBuilder.builder().build().buildHouseList();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        List<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponseList();

        when(houseRepository.findAll()).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        List<HouseResponse> actual = houseService.getAll();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getAllPaginatedShouldReturnSameElementsAsExpected() {
        // given
        int limit = 1;
        int offset = 0;
        List<House> houses = HouseTestBuilder.builder().build().buildHouseList();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        List<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponseList();

        when(houseRepository.findAll(limit, offset)).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        List<HouseResponse> actual = houseService.getAll(limit, offset);

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

        when(houseRepository.findById(uuid)).thenReturn(Optional.of(house));
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
        HouseRequest houseRequest = HouseTestBuilder.builder().build().buildHouseRequest();
        House house = HouseTestBuilder.builder().build().buildHouse();
        House expected = HouseTestBuilder.builder().build().buildHouse();

        when(houseMapper.toHouse(houseRequest)).thenReturn(house);
        when(houseRepository.findById(house.getUuid())).thenReturn(Optional.of(house));

        // when
        houseService.update(houseRequest);

        // then
        verify(houseRepository)
                .update(houseCaptor.capture());
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
    void deleteByIdShouldThrowNotFoundException() {
        // given
        UUID uuid = UUID.fromString("0116a46b-d57b-4bbc-a697-d4a7ace791f5");

        // when
        houseService.deleteById(uuid);

        // then
        verify(houseRepository).deleteById(uuid);
    }

    @Test
    void countShouldReturnExpectedValue() {
        // given
        Integer expected = 100;
        when(houseRepository.count()).thenReturn(100);

        // when
        Integer actual = houseService.count();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getHousesByPersonUuid() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPerson().getUuid();
        List<House> houses = HouseTestBuilder.builder().build().buildHouseList();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        List<HouseResponse> expected = HouseTestBuilder.builder().build().buildHouseResponseList();

        when(houseRepository.findHousesByPersonUuid(uuid)).thenReturn(houses);
        when(houseMapper.toHouseResponse(any())).thenReturn(houseResponse);

        // when
        List<HouseResponse> actual = houseService.getHousesByPersonUuid(uuid);

        //then
        assertThat(actual)
                .isEqualTo(expected);
    }
}