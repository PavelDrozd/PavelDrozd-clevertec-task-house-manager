package ru.clevertec.ecl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.data.HouseTestBuilder;
import ru.clevertec.ecl.data.PersonTestBuilder;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.service.HouseHistoryService;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.PersonService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HouseController.class)
@RequiredArgsConstructor
public class HouseControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private final HouseService houseService;

    @MockBean
    private final PersonService personService;

    @MockBean
    private final HouseHistoryService houseHistoryService;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void getShouldReturnExpectedHouseResponse() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouseRequest().uuid();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();

        when(houseService.getById(uuid))
                .thenReturn(houseResponse);

        // when, then
        mockMvc.perform(get("/houses/" + uuid))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.uuid").value(houseResponse.uuid().toString()),
                        jsonPath("$.country").value(houseResponse.country()),
                        jsonPath("$.area").value(houseResponse.area()),
                        jsonPath("$.city").value(houseResponse.city()),
                        jsonPath("$.street").value(houseResponse.street()),
                        jsonPath("$.number").value(houseResponse.number())
                );
    }

    @SneakyThrows
    @Test
    void getShouldReturnStatusNotFound() {
        // given
        UUID fakeUuid = UUID.fromString("00002dde-9556-4ef5-954c-aeebc42c5056");

        when(houseService.getById(fakeUuid))
                .thenThrow(NotFoundException.class);

        // when, then
        mockMvc.perform(get("/houses/" + fakeUuid))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getAllShouldReturnExpectedHouseResponse() {
        // given
        Page<HouseResponse> housesResponse = HouseTestBuilder.builder().build().buildHouseResponsePage();
        HouseResponse expected = HouseTestBuilder.builder().build().buildHouseResponse();

        when(houseService.getAll(any(Pageable.class)))
                .thenReturn(housesResponse);

        // when, then
        mockMvc.perform(get("/houses")
                        .param("size", "1"))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.content[0].uuid").value(expected.uuid().toString()),
                        jsonPath("$.content[0].country").value(expected.country()),
                        jsonPath("$.content[0].area").value(expected.area()),
                        jsonPath("$.content[0].city").value(expected.city()),
                        jsonPath("$.content[0].street").value(expected.street()),
                        jsonPath("$.content[0].number").value(expected.number())
                );
    }

    @SneakyThrows
    @Test
    void getByHouseShouldReturnExpectedPersonResponse() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouseRequest().uuid();
        Page<PersonResponse> personsResponse = PersonTestBuilder.builder().build().buildPersonResponsePage();
        PersonResponse expected = PersonTestBuilder.builder().build().buildPersonResponse();

        when(personService.getPersonsByHouseUuid(any(UUID.class), any(Pageable.class)))
                .thenReturn(personsResponse);

        // when, then
        mockMvc.perform(get("/houses/" + uuid + "/persons"))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.content[0].uuid").value(expected.uuid().toString()),
                        jsonPath("$.content[0].name").value(expected.name()),
                        jsonPath("$.content[0].surname").value(expected.surname()),
                        jsonPath("$.content[0].sex").value(expected.sex().toString()),
                        jsonPath("$.content[0].passportSeries").value(expected.passportSeries()),
                        jsonPath("$.content[0].passportNumber").value(expected.passportNumber())
                );
    }

    @SneakyThrows
    @Test
    void getByNameMatchesShouldReturnExpectedHouseResponse() {
        // given
        Page<HouseResponse> housesResponse = HouseTestBuilder.builder().build().buildHouseResponsePage();
        HouseResponse expected = HouseTestBuilder.builder().build().buildHouseResponse();

        when(houseService.getByNameMatches(any(String.class), any(Pageable.class)))
                .thenReturn(housesResponse);

        // when, then
        mockMvc.perform(get("/houses/search/Разинская")
                        .param("size", "1"))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.content[0].uuid").value(expected.uuid().toString()),
                        jsonPath("$.content[0].country").value(expected.country()),
                        jsonPath("$.content[0].area").value(expected.area()),
                        jsonPath("$.content[0].city").value(expected.city()),
                        jsonPath("$.content[0].street").value(expected.street()),
                        jsonPath("$.content[0].number").value(expected.number())
                );
    }

    @SneakyThrows
    @Test
    void getTenantsByHouseShouldReturnExpectedPersonResponse() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouseRequest().uuid();
        Page<PersonResponse> personsResponse = PersonTestBuilder.builder().build().buildPersonResponsePage();
        PersonResponse expected = PersonTestBuilder.builder().build().buildPersonResponse();

        when(houseHistoryService.getTenantsByHouseUuid(any(UUID.class), any(Pageable.class)))
                .thenReturn(personsResponse);

        // when, then
        mockMvc.perform(get("/houses/" + uuid + "/tenants"))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.content[0].uuid").value(expected.uuid().toString()),
                        jsonPath("$.content[0].name").value(expected.name()),
                        jsonPath("$.content[0].surname").value(expected.surname()),
                        jsonPath("$.content[0].sex").value(expected.sex().toString()),
                        jsonPath("$.content[0].passportSeries").value(expected.passportSeries()),
                        jsonPath("$.content[0].passportNumber").value(expected.passportNumber())
                );
    }

    @SneakyThrows
    @Test
    void getOwnersByHouseShouldReturnExpectedPersonResponse() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouseRequest().uuid();
        Page<PersonResponse> personsResponse = PersonTestBuilder.builder().build().buildPersonResponsePage();
        PersonResponse expected = PersonTestBuilder.builder().build().buildPersonResponse();

        when(houseHistoryService.getOwnersByHouseUuid(any(UUID.class), any(Pageable.class)))
                .thenReturn(personsResponse);

        // when, then
        mockMvc.perform(get("/houses/" + uuid + "/owners"))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.content[0].uuid").value(expected.uuid().toString()),
                        jsonPath("$.content[0].name").value(expected.name()),
                        jsonPath("$.content[0].surname").value(expected.surname()),
                        jsonPath("$.content[0].sex").value(expected.sex().toString()),
                        jsonPath("$.content[0].passportSeries").value(expected.passportSeries()),
                        jsonPath("$.content[0].passportNumber").value(expected.passportNumber())
                );
    }

    @SneakyThrows
    @Test
    void createShouldReturnExpectedHouseResponse() {
        // given
        HouseRequest houseRequest = HouseTestBuilder.builder().build().buildHouseRequestForCreate();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        String jsonHouseRequest = objectMapper.writeValueAsString(houseRequest);

        when(houseService.create(houseRequest))
                .thenReturn(houseResponse);

        // when, then
        mockMvc.perform(post("/houses")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(jsonHouseRequest))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.country").value(houseResponse.country()),
                        jsonPath("$.area").value(houseResponse.area()),
                        jsonPath("$.city").value(houseResponse.city()),
                        jsonPath("$.street").value(houseResponse.street()),
                        jsonPath("$.number").value(houseResponse.number())
                );
    }

    @SneakyThrows
    @Test
    void updateShouldReturnExpectedHouseResponse() {
        // given
        HouseRequest houseRequest = HouseTestBuilder.builder().build().buildHouseRequestForUpdate();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponseForUpdate();
        String jsonHouseRequest = objectMapper.writeValueAsString(houseRequest);

        when(houseService.update(houseRequest))
                .thenReturn(houseResponse);

        // when, then
        mockMvc.perform(put("/houses")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(jsonHouseRequest))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.uuid").value(houseResponse.uuid().toString()),
                        jsonPath("$.country").value(houseResponse.country()),
                        jsonPath("$.area").value(houseResponse.area()),
                        jsonPath("$.city").value(houseResponse.city()),
                        jsonPath("$.street").value(houseResponse.street()),
                        jsonPath("$.number").value(houseResponse.number())
                );
    }

    @SneakyThrows
    @Test
    void updatePartShouldReturnExpectedHouseResponse() {
        // given
        HouseRequest houseRequest = HouseTestBuilder.builder().build().buildHouseRequestForUpdatePart();
        HouseResponse houseResponse = HouseTestBuilder.builder().build().buildHouseResponseForUpdatePart();
        String jsonHouseRequest = objectMapper.writeValueAsString(houseRequest);

        when(houseService.updatePart(houseRequest))
                .thenReturn(houseResponse);

        // when, then
        mockMvc.perform(patch("/houses")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(jsonHouseRequest))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.uuid").value(houseResponse.uuid().toString()),
                        jsonPath("$.country").value(houseResponse.country()),
                        jsonPath("$.area").value(houseResponse.area()),
                        jsonPath("$.city").value(houseResponse.city()),
                        jsonPath("$.street").value(houseResponse.street()),
                        jsonPath("$.number").value(houseResponse.number())
                );
    }

    @SneakyThrows
    @Test
    void deleteShouldReturnStatusSuccessful() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouseRequest().uuid();

        // when, then
        mockMvc.perform(delete("/houses/" + uuid))
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void deleteShouldReturnStatusNotFound() {
        // given
        UUID fakeUuid = UUID.fromString("00002dde-9556-4ef5-954c-aeebc42c5056");

        doThrow(NotFoundException.class).when(personService).deleteById(fakeUuid);

        // when, then
        mockMvc.perform(delete("/houses/" + fakeUuid))
                .andExpect(status().is2xxSuccessful());
    }

}