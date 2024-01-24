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
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.service.HouseHistoryService;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.PersonService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    private final HouseService houseService;

    private final HouseHistoryService houseHistoryService;

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> get(@PathVariable UUID id) {
        PersonResponse personResponse = personService.getById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PersonResponse>> getAll(Pageable pageable) {
        Page<PersonResponse> persons = personService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(persons);
    }

    @GetMapping("/{id}/houses")
    public ResponseEntity<Page<HouseResponse>> getByPerson(@PathVariable UUID id, Pageable pageable) {
        Page<HouseResponse> housesResponse = houseService.getHousesByPersonUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(housesResponse);
    }

    @GetMapping("/search/")
    public ResponseEntity<Page<PersonResponse>> getByNameMatches(@PathVariable String name, Pageable pageable) {
        Page<PersonResponse> personsResponse = personService.getByNameMatches(name, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personsResponse);
    }

    @GetMapping("/{id}/tenants")
    public ResponseEntity<Page<HouseResponse>> getHousesByTenant(@PathVariable UUID id, Pageable pageable) {
        Page<HouseResponse> personResponses = houseHistoryService.getHousesByTenantUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @GetMapping("/{id}/owners")
    public ResponseEntity<Page<HouseResponse>> getHousesByOwner(@PathVariable UUID id, Pageable pageable) {
        Page<HouseResponse> personResponses = houseHistoryService.getHousesByOwnerUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@RequestBody @Valid PersonRequest personRequest) {
        PersonResponse personResponse = personService.create(personRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personResponse);
    }

    @PutMapping
    public ResponseEntity<PersonResponse> update(@RequestBody @Valid PersonRequest personRequest) {
        PersonResponse personResponse = personService.update(personRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponse);
    }

    @PatchMapping
    public ResponseEntity<PersonResponse> updatePart(@RequestBody @Valid PersonRequest personRequest) {
        PersonResponse personResponse = personService.updatePart(personRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        personService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
