package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.validator.impl.HouseRequestValidator;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository repository;

    private final HouseMapper mapper;

    private final HouseRequestValidator validator;

    @Override
    public HouseResponse create(HouseRequest houseRequest) {
        validator.validate(houseRequest);
        House house = mapper.toHouse(houseRequest);
        House saved = repository.create(house);
        return mapper.toHouseResponse(saved);
    }

    @Override
    public List<HouseResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toHouseResponse)
                .toList();
    }

    @Override
    public List<HouseResponse> getAll(int limit, int offset) {
        return repository.findAll(limit, offset).stream()
                .map(mapper::toHouseResponse)
                .toList();
    }

    @Override
    public HouseResponse getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toHouseResponse)
                .orElseThrow(() -> NotFoundException.of(House.class, id));
    }

    @Override
    public HouseResponse update(HouseRequest houseRequest) {
        validator.validate(houseRequest);
        House house = mapper.toHouse(houseRequest);
        House updated = repository.update(house);
        return mapper.toHouseResponse(house);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
