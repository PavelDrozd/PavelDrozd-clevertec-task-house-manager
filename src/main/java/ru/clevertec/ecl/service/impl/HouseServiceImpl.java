package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.aspect.annotation.Create;
import ru.clevertec.ecl.aspect.annotation.Delete;
import ru.clevertec.ecl.aspect.annotation.Get;
import ru.clevertec.ecl.aspect.annotation.Update;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.service.HouseService;

import java.util.UUID;

/**
 * Implementation of HouseService interface for process House data objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseServiceImpl implements HouseService {

    /** HouseRepository is used to get objects from repository module. */
    private final HouseRepository houseRepository;

    /** HouseMapper for mapping DTO and entity House objects. */
    private final HouseMapper houseMapper;

    /** PersonMapper for mapping DTO and entity Person objects. */
    private final PersonMapper personMapper;

    /**
     * Process HouseRequest object for create new House entity and send it to a repository,
     * then create HouseResponse object.
     *
     * @param houseRequest expected object of type HouseRequest.
     * @return created HouseResponse object.
     */
    @Create
    @Override
    @Transactional
    public HouseResponse create(HouseRequest houseRequest) {
        log.debug("SERVICE: CREATE HOUSE: " + houseRequest);

        House house = houseMapper.toHouse(houseRequest);
        House saved = houseRepository.save(house);

        return houseMapper.toHouseResponse(saved);
    }

    /**
     * Get all House entities from repository paginated with limit and offset, then return as HouseResponse.
     *
     * @param pageable expected an object type of Pageable.
     * @return List of HouseResponse objects.
     */
    @Override
    public Page<HouseResponse> getAll(Pageable pageable) {
        log.debug("SERVICE: GET ALL HOUSES WITH PAGEABLE: " + pageable);

        return houseRepository.findByDeletedFalse(pageable)
                .map(houseMapper::toHouseResponse);
    }

    /**
     * Get House from repository by UUID and return it as HouseResponse.
     *
     * @param uuid expected object type of UUID.
     * @return HouseResponse object.
     */
    @Get
    @Override
    public HouseResponse getByUuid(UUID uuid) {
        log.debug("SERVICE: GET HOUSE BY UUID: " + uuid);

        return houseRepository.findByUuidAndDeletedFalse(uuid)
                .map(houseMapper::toHouseResponse)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));
    }

    /**
     * Update House in repository by accepted HouseRequest object with updated data and return as HouseResponse.
     *
     * @param houseRequest expected an object type of HouseRequest with filled fields.
     * @return updated object HouseResponse.
     */
    @Update
    @Override
    @Transactional
    public HouseResponse update(HouseRequest houseRequest) {
        log.debug("SERVICE: UPDATE HOUSE: " + houseRequest);

        UUID uuid = houseRequest.uuid();
        House exist = houseRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));

        if (isChanged(exist, houseRequest)) {
            return houseMapper.toHouseResponse(exist);
        }

        House house = houseMapper.mergeWithNulls(exist, houseRequest);
        House updatedHouse = houseRepository.save(house);

        return houseMapper.toHouseResponse(updatedHouse);
    }


    /**
     * Update House in repository by accepted HouseRequest object with updated part of data and return as HouseResponse.
     *
     * @param houseRequest expected an object type of HouseRequest with not fulfilled fields.
     * @return updated object HouseResponse.
     */
    @Update
    @Override
    public HouseResponse updatePart(HouseRequest houseRequest) {
        log.debug("SERVICE: UPDATE PART HOUSE: " + houseRequest);

        UUID uuid = houseRequest.uuid();
        House exist = houseRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));

        if (isChanged(exist, houseRequest)) {
            return houseMapper.toHouseResponse(exist);
        }

        House mergedHouse = houseMapper.merge(exist, houseRequest);
        House updatedHouse = houseRepository.save(mergedHouse);

        return houseMapper.toHouseResponse(updatedHouse);
    }

    /**
     * Delete House entity from repository by UUID.
     *
     * @param uuid expected object type of UUID.
     */
    @Delete
    @Override
    @Transactional
    public void deleteByUuid(UUID uuid) {
        log.debug("SERVICE: DELETE HOUSE BY UUID: " + uuid);

        House houseForDelete = houseRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));
        houseForDelete.setDeleted(true);
        houseRepository.save(houseForDelete);
    }

    /**
     * Get houses from repository by Person UUID.
     *
     * @param uuid       expected object type of UUID.
     * @param pageable expected an object type of Pageable.
     * @return List of HouseResponse objects.
     */
    @Override
    public Page<HouseResponse> getHousesByPersonUuid(UUID uuid, Pageable pageable) {
        log.debug("SERVICE: FIND HOUSES BY PERSON UUID: " + uuid + " WITH PAGEABLE: " + pageable);

        return houseRepository.findByTenants_UuidAndDeletedFalseAndTenants_DeletedFalse(uuid, pageable)
                .map(houseMapper::toHouseResponse);
    }

    /**
     * Get houses from repository by any matches name.
     *
     * @param name     expected string name.
     * @param pageable expected an object type of Pageable.
     * @return List of HouseResponse objects.
     */
    @Override
    public Page<HouseResponse> getByNameMatches(String name, Pageable pageable) {
        log.debug("SERVICE: GET HOUSES BY NAME MATCHES: " + name + " WITH PAGEABLE: " + pageable);

        return houseRepository.findByNameMatches(name, pageable)
                .map(houseMapper::toHouseResponse);
    }

    private boolean isChanged(House exist, HouseRequest houseRequest) {
        return houseRequest.country() != null
               && !exist.getCountry().equals(houseRequest.country())

               && houseRequest.area() != null
               && !exist.getArea().equals(houseRequest.area())

               && houseRequest.city() != null
               && !exist.getCity().equals(houseRequest.city())

               && houseRequest.street() != null
               && !exist.getStreet().equals(houseRequest.street())

               && houseRequest.number() != null
               && !exist.getNumber().equals(houseRequest.number());
    }
}
