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
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.service.HouseHistoryService;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.PersonService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
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

@WebMvcTest(PersonController.class)
@RequiredArgsConstructor
public class PersonControllerTest {

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
    void getShouldReturnExpectedPersonResponse() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPersonRequest().uuid();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponse();

        when(personService.getByUuid(uuid))
                .thenReturn(personResponse);

        // when, then
        mockMvc.perform(get("/persons/" + uuid))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.uuid").value(personResponse.uuid().toString()),
                        jsonPath("$.name").value(personResponse.name()),
                        jsonPath("$.surname").value(personResponse.surname()),
                        jsonPath("$.sex").value(personResponse.sex().toString()),
                        jsonPath("$.passportSeries").value(personResponse.passportSeries()),
                        jsonPath("$.passportNumber").value(personResponse.passportNumber())
                );
    }

    @SneakyThrows
    @Test
    void getShouldReturnStatusNotFound() {
        // given
        UUID fakeUuid = UUID.fromString("00017dfd-a27d-45b6-8c72-a15538b8216e");

        when(personService.getByUuid(fakeUuid))
                .thenThrow(NotFoundException.class);

        // when, then
        mockMvc.perform(get("/persons/" + fakeUuid))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getAllShouldReturnExpectedPersonResponse() {
        // given
        Page<PersonResponse> personsResponse = PersonTestBuilder.builder().build().buildPersonResponsePage();
        PersonResponse expected = PersonTestBuilder.builder().build().buildPersonResponse();

        when(personService.getAll(any(Pageable.class)))
                .thenReturn(personsResponse);

        // when, then
        mockMvc.perform(get("/persons")
                        .param("size", "1"))
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
    void getByPersonShouldReturnExpectedPersonResponse() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPersonRequest().uuid();
        Page<HouseResponse> housesResponse = HouseTestBuilder.builder().build().buildHouseResponsePage();
        HouseResponse expected = HouseTestBuilder.builder().build().buildHouseResponse();

        when(houseService.getHousesByPersonUuid(any(UUID.class), any(Pageable.class)))
                .thenReturn(housesResponse);

        // when, then
        mockMvc.perform(get("/persons/" + uuid + "/houses"))
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
    void getByNameMatchesShouldReturnExpectedPersonResponse() {
        // given
        Page<PersonResponse> personsResponse = PersonTestBuilder.builder().build().buildPersonResponsePage();
        PersonResponse expected = PersonTestBuilder.builder().build().buildPersonResponse();

        when(personService.getByNameMatches(any(String.class), any(Pageable.class)))
                .thenReturn(personsResponse);

        // when, then
        mockMvc.perform(get("/persons/search/Лазарева")
                        .param("size", "1"))
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
    void getHousesByTenantShouldReturnExpectedHouseResponse() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPersonRequest().uuid();
        Page<HouseResponse> housesResponse = HouseTestBuilder.builder().build().buildHouseResponsePage();
        HouseResponse expected = HouseTestBuilder.builder().build().buildHouseResponse();

        when(houseHistoryService.getHousesByTenantUuid(any(UUID.class), any(Pageable.class)))
                .thenReturn(housesResponse);

        // when, then
        mockMvc.perform(get("/persons/" + uuid + "/tenants"))
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
    void getHousesByOwnerShouldReturnExpectedHouseResponse() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPersonRequest().uuid();
        Page<HouseResponse> housesResponse = HouseTestBuilder.builder().build().buildHouseResponsePage();
        HouseResponse expected = HouseTestBuilder.builder().build().buildHouseResponse();

        when(houseHistoryService.getHousesByOwnerUuid(any(UUID.class), any(Pageable.class)))
                .thenReturn(housesResponse);

        // when, then
        mockMvc.perform(get("/persons/" + uuid + "/owners"))
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
    void createShouldReturnExpectedPersonResponse() {
        // given
        PersonRequest personRequest = PersonTestBuilder.builder().build().buildPersonRequestForCreate();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponseForCreate();
        String jsonPersonRequest = objectMapper.writeValueAsString(personRequest);

        when(personService.create(personRequest))
                .thenReturn(personResponse);

        // when, then
        mockMvc.perform(post("/persons")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(jsonPersonRequest))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.name").value(personResponse.name()),
                        jsonPath("$.surname").value(personResponse.surname()),
                        jsonPath("$.sex").value(personResponse.sex().toString()),
                        jsonPath("$.passportSeries").value(personResponse.passportSeries()),
                        jsonPath("$.passportNumber").value(personResponse.passportNumber())
                );
    }

    @SneakyThrows
    @Test
    void updateShouldReturnExpectedPersonResponse() {
        // given
        PersonRequest personRequest = PersonTestBuilder.builder().build().buildPersonRequestForUpdate();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponseForUpdate();
        String jsonPersonRequest = objectMapper.writeValueAsString(personRequest);

        when(personService.update(personRequest))
                .thenReturn(personResponse);

        // when, then
        mockMvc.perform(put("/persons")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(jsonPersonRequest))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.name").value(personResponse.name()),
                        jsonPath("$.surname").value(personResponse.surname()),
                        jsonPath("$.sex").value(personResponse.sex().toString()),
                        jsonPath("$.passportSeries").value(personResponse.passportSeries()),
                        jsonPath("$.passportNumber").value(personResponse.passportNumber())
                );
    }

    @SneakyThrows
    @Test
    void updatePartShouldReturnExpectedPersonResponse() {
        // given
        PersonRequest personRequest = PersonTestBuilder.builder().build().buildPersonRequestForUpdatePart();
        PersonResponse personResponse = PersonTestBuilder.builder().build().buildPersonResponseForUpdatePart();
        String jsonPersonRequest = objectMapper.writeValueAsString(personRequest);

        when(personService.updatePart(personRequest))
                .thenReturn(personResponse);

        // when, then
        mockMvc.perform(patch("/persons")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(jsonPersonRequest))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.name").value(personResponse.name()),
                        jsonPath("$.surname").value(personResponse.surname()),
                        jsonPath("$.sex").value(personResponse.sex().toString()),
                        jsonPath("$.passportSeries").value(personResponse.passportSeries()),
                        jsonPath("$.passportNumber").value(personResponse.passportNumber())
                );
    }

    @SneakyThrows
    @Test
    void deleteShouldReturnStatusSuccessful() {
        // given
        UUID uuid = PersonTestBuilder.builder().build().buildPersonRequest().uuid();

        // when, then
        mockMvc.perform(delete("/persons/" + uuid))
                .andExpect(status().is2xxSuccessful());
    }
}
