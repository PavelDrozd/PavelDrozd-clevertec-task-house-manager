package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.PersonRepository;
import ru.clevertec.ecl.service.PersonService;
import ru.clevertec.ecl.validator.impl.PersonRequestValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    private final PersonMapper mapper;

    private final PersonRequestValidator validator;

    @Override
    public PersonResponse create(PersonRequest personRequest) {
        validator.validate(personRequest);
        Person person = mapper.toPerson(personRequest);
        Person created = repository.create(person);
        return mapper.toPersonResponse(created);
    }

    @Override
    public List<PersonResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toPersonResponse)
                .toList();
    }

    @Override
    public List<PersonResponse> getAll(int limit, int offset) {
        return repository.findAll(limit, offset).stream()
                .map(mapper::toPersonResponse)
                .toList();
    }

    @Override
    public PersonResponse getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toPersonResponse)
                .orElseThrow(() -> NotFoundException.of(Person.class, id));
    }

    @Override
    @Transactional
    public PersonResponse update(PersonRequest personRequest) {
        validator.validate(personRequest);
        Person person = mapper.toPerson(personRequest);
        person.setUpdateDate(LocalDateTime.now());
        Person updated = repository.update(person);
        return mapper.toPersonResponse(updated);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
