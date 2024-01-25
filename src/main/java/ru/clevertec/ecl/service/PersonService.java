package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;

import java.util.UUID;

public interface PersonService extends AbstractService<UUID, PersonRequest, PersonResponse> {

    Page<PersonResponse> getPersonsByHouseUuid(UUID id, Pageable pageable);

    Page<PersonResponse> getByNameMatches(String name, Pageable pageable);
}
