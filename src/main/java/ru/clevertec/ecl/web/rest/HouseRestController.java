package ru.clevertec.ecl.web.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.service.HouseService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
public class HouseRestController {

    private final HouseService service;

    @GetMapping("/{id}")
    public HouseResponse get(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    public List<HouseResponse> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<HouseResponse> create(@RequestBody @Valid HouseRequest houseRequest, Model model) {
        HouseResponse house = service.create(houseRequest);
        model.addAttribute("house", house);
        return buildResponseHouse(house, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HouseResponse> update(@RequestBody @Valid HouseRequest houseRequest, Model model) {
        HouseResponse house = service.update(houseRequest);
        model.addAttribute("house", house);
        return buildResponseHouse(house, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteById(id);
    }

    private ResponseEntity<HouseResponse> buildResponseHouse(HouseResponse houseResponse, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .location(getLocation(houseResponse))
                .body(houseResponse);
    }

    private URI getLocation(HouseResponse houseResponse) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/houses/{id}")
                .buildAndExpand(houseResponse.uuid())
                .toUri();
    }
}
