package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.service.HouseService;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of HouseService interface for process House data objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    /** HouseRepository is used to get objects from repository module. */
    private final HouseRepository houseRepository;

    /** HouseMapper for mapping DTO and entity House objects. */
    private final HouseMapper mapper;

    /**
     * Process HouseRequest object for create new House entity and send it to repository,
     * then create HouseResponse object.
     *
     * @param houseRequest expected object of type HouseRequest.
     * @return created HouseResponse object.
     */
    @Override
    @Transactional
    public HouseResponse create(HouseRequest houseRequest) {
        log.debug("SERVICE: CREATE HOUSE: " + houseRequest);
        House house = mapper.toHouse(houseRequest);
        House saved = houseRepository.create(house);
        return mapper.toHouseResponse(saved);
    }

    /**
     * Get all House entities from repository and return as HouseResponse.
     *
     * @return List of HouseResponse objects.
     */
    @Override
    public List<HouseResponse> getAll() {
        log.debug("SERVICE: GET ALL HOUSES.");
        return houseRepository.findAll().stream().map(mapper::toHouseResponse).toList();
    }

    /**
     * Get all House entities from repository paginated with limit and offset, then return as HouseResponse.
     *
     * @param limit  expected integer value of limit.
     * @param offset expected integer value of offset.
     * @return List of HouseResponse objects.
     */
    @Override
    public List<HouseResponse> getAll(int limit, int offset) {
        log.debug("SERVICE: GET ALL HOUSES WITH LIMIT: " + limit + " OFFSET: " + offset);
        return houseRepository.findAll(limit, offset).stream().map(mapper::toHouseResponse).toList();
    }

    /**
     * Get House from repository by UUID and return it as HouseResponse.
     *
     * @param id expected object type of UUID.
     * @return HouseResponse object.
     */
    @Override
    public HouseResponse getById(UUID id) {
        log.debug("SERVICE: GET HOUSE BY UUID: " + id);
        return houseRepository.findById(id).map(mapper::toHouseResponse)
                .orElseThrow(() -> NotFoundException.of(House.class, id));
    }

    /**
     * Update House in repository by accept HouseRequest object with updated data and return as HouseResponse.
     *
     * @param houseRequest expected object type of HouseRequest with filled fields.
     * @return updated object HouseResponse.
     */
    @Override
    @Transactional
    public HouseResponse update(HouseRequest houseRequest) {
        log.debug("SERVICE: UPDATE HOUSE: " + houseRequest);
        UUID id = houseRequest.uuid();
        House exist = houseRepository.findById(id).orElseThrow(() -> NotFoundException.of(House.class, id));

        if (isChanged(exist, houseRequest)) {
            return getById(houseRequest.uuid());
        }
        House house = mergeHouse(exist, houseRequest);
        House updated = houseRepository.update(house);
        return mapper.toHouseResponse(house);
    }

    /**
     * Delete House entity from repository by UUID.
     *
     * @param id expected object type of UUID.
     */
    @Override
    public void deleteById(UUID id) {
        log.debug("SERVICE: DELETE HOUSE BY UUID: " + id);
        houseRepository.deleteById(id);
    }

    /**
     * Count all entities in repository and return it as Integer.
     *
     * @return Integer value of objects being counted.
     */
    @Override
    public int count() {
        log.debug("SERVICE: COUNT HOUSES.");
        return houseRepository.count();
    }

    /**
     * Get houses from repository by Person UUID.
     *
     * @param id expected object type of UUID.
     * @return List of HouseResponse objects.
     */
    @Override
    public List<HouseResponse> getHousesByPersonUuid(UUID id) {
        log.debug("SERVICE: FIND HOUSES BY PERSON UUID: " + id);
        return houseRepository.findHousesByPersonUuid(id).stream().map(mapper::toHouseResponse).toList();
    }

    private boolean isChanged(House exist, HouseRequest houseRequest) {
        return exist.getCountry().equals(houseRequest.country())
               && exist.getArea().equals(houseRequest.area())
               && exist.getCity().equals(houseRequest.city())
               && exist.getStreet().equals(houseRequest.street())
               && exist.getNumber().equals(houseRequest.number());
    }

    private House mergeHouse(House exist, HouseRequest houseRequest) {
        House house = mapper.toHouse(houseRequest);
        house.setId(exist.getId());
        house.setCreateDate(exist.getCreateDate());
        return house;
    }
}
