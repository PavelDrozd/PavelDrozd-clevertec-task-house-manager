package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.service.HouseHistoryService;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.PersonService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
public class HouseController {

    private final HouseService houseService;

    private final PersonService personService;

    private final HouseHistoryService houseHistoryService;

    @GetMapping("/{id}")
    public ResponseEntity<HouseResponse> get(@PathVariable UUID id) {
        HouseResponse houseResponse = houseService.getById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houseResponse);
    }

    @GetMapping
    public ResponseEntity<Page<HouseResponse>> getAll(Pageable pageable) {
        Page<HouseResponse> houses = houseService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houses);
    }

    @GetMapping("/{id}/persons")
    public ResponseEntity<Page<PersonResponse>> getByHouse(@PathVariable UUID id, Pageable pageable) {
        Page<PersonResponse> personsResponse = personService.getPersonsByHouseUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personsResponse);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<Page<HouseResponse>> getByNameMatches(@PathVariable String name, Pageable pageable) {
        Page<HouseResponse> housesResponse = houseService.getByNameMatches(name, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(housesResponse);
    }

    @GetMapping("/{id}/tenants")
    public ResponseEntity<Page<PersonResponse>> getTenantsByHouse(@PathVariable UUID id, Pageable pageable) {
        Page<PersonResponse> personResponses = houseHistoryService.getTenantsByHouseUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @GetMapping("/{id}/owners")
    public ResponseEntity<Page<PersonResponse>> getOwnersByHouse(@PathVariable UUID id, Pageable pageable) {
        Page<PersonResponse> personResponses = houseHistoryService.getOwnersByHouseUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @PostMapping
    public ResponseEntity<HouseResponse> create(@RequestBody @Valid HouseRequest houseRequest) {
        HouseResponse houseResponse = houseService.create(houseRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(houseResponse);
    }

    @PutMapping
    public ResponseEntity<HouseResponse> update(@RequestBody @Valid HouseRequest houseRequest) {
        HouseResponse houseResponse = houseService.update(houseRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houseResponse);
    }

    @PatchMapping
    public ResponseEntity<HouseResponse> updatePart(@RequestBody @Valid HouseRequest houseRequest) {
        HouseResponse houseResponse = houseService.updatePart(houseRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houseResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        houseService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
