package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
        return ResponseEntity.status(HttpStatus.OK)
                .body(houseService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<HouseResponse>> getAll(Pageable pageable) {
        Page<HouseResponse> houses = houseService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houses);
    }

    @GetMapping("/{id}/persons")
    public ResponseEntity<List<PersonResponse>> getByHouse(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(houseService.getPersonsByHouseUuid(id));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<HouseResponse>> getByNameMatches(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(houseService.getByNameMatches(name));
    }

    @PostMapping
    public ResponseEntity<HouseResponse> create(@RequestBody HouseRequest houseRequest) {
        HouseResponse house = houseService.create(houseRequest);

        return buildResponseHouse(house, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HouseResponse> update(@RequestBody HouseRequest houseRequest) {
        HouseResponse house = houseService.update(houseRequest);

        return buildResponseHouse(house, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        houseService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<HouseResponse> buildResponseHouse(HouseResponse houseResponse, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(houseResponse);
    }
}
