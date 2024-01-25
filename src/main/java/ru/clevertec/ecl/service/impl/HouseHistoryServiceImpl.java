package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.enums.Type;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.repository.PersonRepository;
import ru.clevertec.ecl.service.HouseHistoryService;

import java.util.UUID;

/**
 * Implementation of HouseHistoryService interface for process HouseHistory data objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseHistoryServiceImpl implements HouseHistoryService {

    /** PersonRepository is used to get objects from repository module. */
    private final PersonRepository personRepository;

    /** HouseRepository is used to get objects from repository module. */
    private final HouseRepository houseRepository;

    /** HouseMapper for mapping DTO and entity House objects. */
    private final HouseMapper houseMapper;

    /** PersonMapper for mapping DTO and entity Person objects. */
    private final PersonMapper personMapper;

    /**
     * Get tenants from repository by House UUID.
     *
     * @param uuid     expected object type of UUID.
     * @param pageable expected an object type of Pageable.
     * @return List of PersonResponse objects.
     */
    @Override
    public Page<PersonResponse> getTenantsByHouseUuid(UUID uuid, Pageable pageable) {
        log.debug("SERVICE: GET ALL HISTORY OF TENANTS BY HOUSE UUID: " + uuid + " WITH PAGEABLE: " + pageable);

        return personRepository.findPersonsByHouseUuidAndType(uuid, Type.TENANT, pageable)
                .map(personMapper::toPersonResponse);
    }

    /**
     * Get owners from repository by House UUID.
     *
     * @param uuid     expected object type of UUID.
     * @param pageable expected an object type of Pageable.
     * @return List of PersonResponse objects.
     */
    @Override
    public Page<PersonResponse> getOwnersByHouseUuid(UUID uuid, Pageable pageable) {
        log.debug("SERVICE: GET ALL HISTORY OF OWNERS BY HOUSE UUID: " + uuid + " WITH PAGEABLE: " + pageable);

        return personRepository.findPersonsByHouseUuidAndType(uuid, Type.OWNER, pageable)
                .map(personMapper::toPersonResponse);
    }

    /**
     * Get tenant houses from repository by Person UUID.
     *
     * @param uuid     expected object type of UUID.
     * @param pageable expected an object type of Pageable.
     * @return List of HouseResponse objects.
     */
    @Override
    public Page<HouseResponse> getHousesByTenantUuid(UUID uuid, Pageable pageable) {
        log.debug("SERVICE: GET ALL HISTORY OF HOUSES BY TENANT UUID: " + uuid + " WITH PAGEABLE: " + pageable);

        return houseRepository.findHousesByPersonUuidAndType(uuid, Type.TENANT, pageable)
                .map(houseMapper::toHouseResponse);
    }

    /**
     * Get owner houses from repository by House UUID.
     *
     * @param uuid     expected object type of UUID.
     * @param pageable expected an object type of Pageable.
     * @return List of HouseResponse objects.
     */
    @Override
    public Page<HouseResponse> getHousesByOwnerUuid(UUID uuid, Pageable pageable) {
        log.debug("SERVICE: GET ALL HISTORY OF HOUSES BY OWNER UUID: " + uuid + " WITH PAGEABLE: " + pageable);

        return houseRepository.findHousesByPersonUuidAndType(uuid, Type.OWNER, pageable)
                .map(houseMapper::toHouseResponse);
    }
}
