package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.data.page.Paging;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.pagination.Pagination;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.validator.impl.HouseRequestValidator;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
public class HouseRestController {

    private final HouseService service;
    private final Pagination pagination;
    private final HouseRequestValidator validator;

    @GetMapping("/{id}")
    public ResponseEntity<HouseResponse> get(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<HouseResponse>> getAll(@RequestParam(required = false) Integer page,
                                                      @RequestParam(required = false) Integer pageSize,
                                                      @RequestParam(required = false) Integer offset) {

        int totalEntities = service.count();
        Paging paging = pagination.getPaging(pageSize, offset, page, totalEntities);

        List<HouseResponse> houses = service.getAll(paging.limit(), paging.offset());

        return ResponseEntity.status(HttpStatus.OK)
                .body(houses);
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<List<HouseResponse>> getByPerson(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getHousesByPersonUuid(id));
    }

    @PostMapping
    public ResponseEntity<HouseResponse> create(@RequestBody HouseRequest houseRequest) {
        validator.validate(houseRequest);
        HouseResponse house = service.create(houseRequest);

        return buildResponseHouse(house, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HouseResponse> update(@RequestBody HouseRequest houseRequest) {
        validator.validate(houseRequest);
        HouseResponse house = service.update(houseRequest);

        return buildResponseHouse(house, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<HouseResponse> buildResponseHouse(HouseResponse houseResponse, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(houseResponse);
    }
}
