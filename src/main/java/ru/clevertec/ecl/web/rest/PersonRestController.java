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
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.service.PersonService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonRestController {

    private final PersonService service;

    @GetMapping("/{id}")
    public PersonResponse get(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    public List<PersonResponse> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@RequestBody @Valid PersonRequest personRequest, Model model) {
        PersonResponse person = service.create(personRequest);
        model.addAttribute("person", person);
        return buildResponsePerson(person, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PersonResponse> update(@RequestBody @Valid PersonRequest personRequest, Model model) {
        PersonResponse person = service.update(personRequest);
        model.addAttribute("person", person);
        return buildResponsePerson(person, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteById(id);
    }

    private ResponseEntity<PersonResponse> buildResponsePerson(PersonResponse personResponse, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .location(getLocation(personResponse))
                .body(personResponse);
    }

    private URI getLocation(PersonResponse personResponse) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/persons/{id}")
                .buildAndExpand(personResponse.uuid())
                .toUri();
    }
}
