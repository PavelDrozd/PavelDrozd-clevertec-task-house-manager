package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.service.PersonService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> get(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(personService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PersonResponse>> getAll(Pageable pageable) {
        Page<PersonResponse> persons = personService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(persons);
    }

    @GetMapping("/{id}/houses")
    public ResponseEntity<List<HouseResponse>> getByPerson(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(personService.getHousesByPersonUuid(id));
    }

    @GetMapping("/search/")
    public ResponseEntity<List<PersonResponse>> getByNameMatches(@PathVariable String name){
        return ResponseEntity.status(HttpStatus.OK)
                .body(personService.getByNameMatches(name));
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@RequestBody PersonRequest personRequest, Model model) {
        PersonResponse person = personService.create(personRequest);

        return buildResponsePerson(person, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PersonResponse> update(@RequestBody PersonRequest personRequest, Model model) {
        PersonResponse person = personService.update(personRequest);

        return buildResponsePerson(person, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        personService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<PersonResponse> buildResponsePerson(PersonResponse personResponse, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(personResponse);
    }

}
