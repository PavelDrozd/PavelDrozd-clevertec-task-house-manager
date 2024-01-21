package ru.clevertec.ecl.controller;

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
import ru.clevertec.ecl.service.HouseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
public class HouseController {

    private final HouseService houseService;

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
    public ResponseEntity<List<PersonResponse>> getByHouse(@PathVariable UUID id) {
        List<PersonResponse> personsResponse = houseService.getPersonsByHouseUuid(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personsResponse);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<HouseResponse>> getByNameMatches(@PathVariable String name) {
        List<HouseResponse> housesResponse = houseService.getByNameMatches(name);

        return ResponseEntity.status(HttpStatus.OK)
                .body(housesResponse);
    }

    @PostMapping
    public ResponseEntity<HouseResponse> create(@RequestBody HouseRequest houseRequest) {
        HouseResponse houseResponse = houseService.create(houseRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(houseResponse);
    }

    @PutMapping
    public ResponseEntity<HouseResponse> update(@RequestBody HouseRequest houseRequest) {
        HouseResponse houseResponse = houseService.update(houseRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houseResponse);
    }

    @PatchMapping
    public ResponseEntity<HouseResponse> updatePart(@RequestBody HouseRequest houseRequest) {
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
