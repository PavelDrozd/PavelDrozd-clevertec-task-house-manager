package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.service.PersonService;

@Mapper(componentModel = "spring", uses = PersonService.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "tenantHouse", ignore = true)
    @Mapping(target = "ownerHouses", ignore = true)
    Person toPerson(PersonRequest personRequest);

    PersonResponse toPersonResponse(Person person);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "tenantHouse", ignore = true)
    @Mapping(target = "ownerHouses", ignore = true)
    Person merge(@MappingTarget Person person, PersonRequest personRequest);

    @Mapping(target = "name",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "surname",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "sex",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "passportSeries",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "passportNumber",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "tenantHouse", ignore = true)
    @Mapping(target = "ownerHouses", ignore = true)
    Person mergeWithNulls(@MappingTarget Person person, PersonRequest personRequest);
}