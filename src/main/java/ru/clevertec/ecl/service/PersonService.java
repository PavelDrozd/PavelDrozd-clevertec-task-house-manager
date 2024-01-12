package ru.clevertec.ecl.service;

import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;

import java.util.UUID;

public interface PersonService extends AbstractService<UUID, PersonRequest, PersonResponse> {
}
