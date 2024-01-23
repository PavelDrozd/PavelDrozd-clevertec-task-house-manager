package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.enums.Type;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseHistoryRepository;
import ru.clevertec.ecl.service.HouseHistoryService;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of HouseHistoryService interface for process HouseHistory data objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseHistoryServiceImpl implements HouseHistoryService {

    /** HouseRepository is used to get objects from repository module. */
    private final HouseHistoryRepository houseHistoryRepository;

    /** HouseMapper for mapping DTO and entity House objects. */
    private final HouseMapper houseMapper;

    /** PersonMapper for mapping DTO and entity Person objects. */
    private final PersonMapper personMapper;

    /**
     * Get tenants from repository by House UUID.
     *
     * @param uuid expected object type of UUID.
     * @return List of PersonResponse objects.
     */
    @Override
    public List<PersonResponse> getTenantsByHouseUuid(UUID uuid) {
        return houseHistoryRepository.findPersonsByHouseUuidAndType(uuid, Type.TENANT).stream()
                .map(personMapper::toPersonResponse)
                .toList();
    }

    /**
     * Get owners from repository by House UUID.
     *
     * @param uuid expected object type of UUID.
     * @return List of PersonResponse objects.
     */
    @Override
    public List<PersonResponse> getOwnersByHouseUuid(UUID uuid) {
        return houseHistoryRepository.findPersonsByHouseUuidAndType(uuid, Type.OWNER).stream()
                .map(personMapper::toPersonResponse)
                .toList();
    }

    /**
     * Get tenant houses from repository by Person UUID.
     *
     * @param uuid expected object type of UUID.
     * @return List of HouseResponse objects.
     */
    @Override
    public List<HouseResponse> getHousesByTenantUuid(UUID uuid) {
        return houseHistoryRepository.findHousesByPersonUuidAndType(uuid, Type.TENANT).stream()
                .map(houseMapper::toHouseResponse)
                .toList();
    }

    /**
     * Get owner houses from repository by House UUID.
     *
     * @param uuid expected object type of UUID.
     * @return List of HouseResponse objects.
     */
    @Override
    public List<HouseResponse> getHousesByOwnerUuid(UUID uuid) {
        return houseHistoryRepository.findHousesByPersonUuidAndType(uuid, Type.OWNER).stream()
                .map(houseMapper::toHouseResponse)
                .toList();
    }
}
