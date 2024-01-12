package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.service.HouseService;

@Mapper(componentModel = "spring", uses = HouseService.class)
public interface HouseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    House toHouse(HouseRequest houseRequest);

    HouseResponse toHouseResponse(House house);
}
