package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.data.HouseDto;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.service.HouseService;

@Mapper(componentModel = "spring", uses = HouseService.class)
public interface HouseMapper {

    @Mapping(target = "id", ignore = true)
    House toHouse(HouseDto houseDto);

    HouseDto toHouseDto(House house);
}
