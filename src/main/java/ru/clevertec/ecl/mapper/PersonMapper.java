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
    @Mapping(target = "residentHouse", source = "residentHouseRequest")
    @Mapping(target = "ownerHouses", source = "ownerHousesRequest")
    Person toPerson(PersonRequest personRequest);

    PersonResponse toPersonResponse(Person person);

    @Mapping(target = "residentHouse", source = "residentHouseRequest")
    @Mapping(target = "ownerHouses", source = "ownerHousesRequest")
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
    @Mapping(target = "residentHouse", source = "residentHouseRequest",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "ownerHouses", source = "ownerHousesRequest",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Person mergeWithNulls(@MappingTarget Person person, PersonRequest personRequest);
}