package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.service.PersonService;

@Mapper(componentModel = "spring", uses = PersonService.class)
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Person toPerson(PersonRequest personRequest);

    PersonResponse toPersonResponse(Person person);
}
