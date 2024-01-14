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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.ecl.data.page.Paging;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.pagination.Pagination;
import ru.clevertec.ecl.service.PersonService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonRestController {

    private final PersonService service;

    private final Pagination pagination;

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> get(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAll(@RequestParam(required = false) Integer page,
                                                       @RequestParam(required = false) Integer pageSize,
                                                       @RequestParam(required = false) Integer offset,
                                                       Model model) {
        int totalEntities = service.count();
        Paging paging = pagination.getPaging(pageSize, offset, page, totalEntities);

        List<PersonResponse> persons = service.getAll(paging.limit(), paging.offset());

        model.addAttribute("persons", persons);
        model.addAttribute("page", paging.page());
        model.addAttribute("total", paging.totalPages());

        return ResponseEntity.status(HttpStatus.OK)
                .body(persons);
    }

    @GetMapping("/house/{id}")
    public ResponseEntity<List<PersonResponse>> getByHouse(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getPersonsByHouseUuid(id));
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@RequestBody PersonRequest personRequest, Model model) {
        PersonResponse person = service.create(personRequest);
        model.addAttribute("person", person);
        return buildResponsePerson(person, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PersonResponse> update(@RequestBody PersonRequest personRequest, Model model) {
        PersonResponse person = service.update(personRequest);
        model.addAttribute("person", person);
        return buildResponsePerson(person, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
