package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.service.HouseService;

@Mapper(componentModel = "spring", uses = HouseService.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HouseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    House toHouse(HouseRequest houseRequest);

    @Mapping(target = "residentsResponse", ignore = true)
    HouseResponse toHouseResponse(House house);
}
