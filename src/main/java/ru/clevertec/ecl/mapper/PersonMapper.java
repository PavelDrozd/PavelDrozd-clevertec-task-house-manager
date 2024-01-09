package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.data.PersonDto;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.service.PersonService;

@Mapper(componentModel = "spring", uses = PersonService.class)
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    Person toPerson(PersonDto personDto);

    PersonDto toPersonDto(Person person);
}
