package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.repository.PersonRepository;
import ru.clevertec.ecl.service.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Implementation of PersonService interface for process Person data objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    /** PersonRepository is used to get objects from repository module. */
    private final PersonRepository personRepository;

    /** HouseRepository is used to get objects from repository module. */
    private final HouseRepository houseRepository;

    /** PersonMapper for mapping DTO and entity Person objects. */
    private final PersonMapper personMapper;

    /** HouseMapper for mapping DTO and entity House objects. */
    private final HouseMapper houseMapper;

    /**
     * Process PersonRequest object for create new Person entity and send it to repository,
     * then create PersonResponse object.
     *
     * @param personRequest expected object of type PersonRequest.
     * @return created PersonResponse object.
     */
    @Override
    @Transactional
    public PersonResponse create(PersonRequest personRequest) {
        log.debug("SERVICE: CREATE PERSON: " + personRequest);
        UUID houseId = personRequest.residentHousesRequest().uuid();
        House house = houseRepository.findByUuid(houseId)
                .orElseThrow(() -> NotFoundException.of(House.class, houseId));

        Person person = personMapper.toPerson(personRequest);
        person.setResidentHouse(house);

        Person created = personRepository.save(person);
        return personMapper.toPersonResponse(created);
    }

    /**
     * Get all Person entities from repository and return as PersonResponse.
     *
     * @return List of PersonResponse objects.
     */
    @Override
    public List<PersonResponse> getAll() {
        log.debug("SERVICE: GET ALL PERSONS.");
        return personRepository.findAll().stream()
                .map(personMapper::toPersonResponse)
                .toList();
    }

    /**
     * Get all Person entities from repository paginated with limit and offset, then return as PersonResponse.
     *
     * @param pageable expected object type of Pageable.
     * @return List of PersonResponse objects.
     */
    @Override
    public Page<PersonResponse> getAll(Pageable pageable) {
        log.debug("SERVICE: FIND ALL PERSONS WITH PAGEABLE: " + pageable);
        return personRepository.findAll(pageable)
                .map(personMapper::toPersonResponse);
    }

    /**
     * Get Person from repository by UUID and return it as PersonResponse.
     *
     * @param id expected object type of UUID.
     * @return PersonResponse object.
     */
    @Override
    public PersonResponse getById(UUID id) {
        log.debug("SERVICE: GET PERSON BY UUID: " + id);
        return personRepository.findByUuid(id)
                .map(personMapper::toPersonResponse)
                .orElseThrow(() -> NotFoundException.of(Person.class, id));
    }

    /**
     * Update Person in repository by accept PersonRequest object with updated data and return as PersonResponse.
     *
     * @param personRequest expected object type of PersonRequest with filled fields.
     * @return updated object PersonResponse.
     */
    @Override
    @Transactional
    public PersonResponse update(PersonRequest personRequest) {
        log.debug("SERVICE: UPDATE PERSON: " + personRequest);
        UUID id = personRequest.uuid();
        Person exist = personRepository.findByUuid(id)
                .orElseThrow(() -> NotFoundException.of(Person.class, id));

        if (isChanged(exist, personRequest)) {
            return personMapper.toPersonResponse(exist);
        }
        Person person = mergePerson(exist, personRequest);
        Person updated = personRepository.save(person);
        return personMapper.toPersonResponse(
                personRepository.findByUuid(updated.getUuid())
                        .orElseThrow(() -> NotFoundException.of(Person.class, id)));
    }

    /**
     * Delete Person entity from repository by UUID.
     *
     * @param id expected object type of UUID.
     */
    @Override
    public void deleteById(UUID id) {
        log.debug("SERVICE: DELETE PERSON BY UUID: " + id);
        personRepository.deleteByUuid(id);
    }

    /**
     * Get residents of house from repository by House UUID.
     *
     * @param id expected object type of UUID.
     * @return List of PersonResponse objects.
     */
    @Override
    public List<HouseResponse> getHousesByPersonUuid(UUID id) {
        log.debug("SERVICE: GET PERSONS BY HOUSE UUID: " + id);
        return personRepository.findHousesByPersonUuid(id).stream()
                .map(houseMapper::toHouseResponse)
                .toList();
    }

    @Override
    public List<PersonResponse> getByNameMatches(String name) {
        return personRepository.findByNameMatches(name).stream()
                .map(personMapper::toPersonResponse)
                .toList();
    }

    private boolean isChanged(Person exist, PersonRequest personRequest) {
        return exist.getName().equals(personRequest.name())
               && exist.getSurname().equals(personRequest.surname())
               && exist.getPassportSeries().equals(personRequest.passportSeries())
               && exist.getPassportNumber().equals(personRequest.passportNumber())
               && exist.getSex().equals(personRequest.sex())
               && personRequest.residentHousesRequest() != null
               && exist.getResidentHouse().getUuid().equals(personRequest.residentHousesRequest().uuid());
    }

    private Person mergePerson(Person exist, PersonRequest personRequest) {
        Person person = personMapper.toPerson(personRequest);
        person.setId(exist.getId());
        person.setCreateDate(exist.getCreateDate());

        setHouseToPerson(exist, personRequest, person);
        setHousesToPerson(exist, personRequest, person);

        return person;
    }

    private void setHouseToPerson(Person exist, PersonRequest personRequest, Person person) {
        HouseRequest houseRequest = personRequest.residentHousesRequest();

        if (houseRequest == null) {
            person.setResidentHouse(exist.getResidentHouse());

        } else {
            House house = houseRepository.findByUuid(houseRequest.uuid())
                    .orElseThrow(() -> NotFoundException.of(House.class, houseRequest.uuid()));
            person.setResidentHouse(house);
        }
    }

    private void setHousesToPerson(Person exist, PersonRequest personRequest, Person person) {
        List<HouseRequest> houseRequests = personRequest.ownerHousesRequest();

        List<House> houses = new ArrayList<>();
        if (houseRequests == null) {
            person.setOwnerHouses(exist.getOwnerHouses());

        } else {
            for (HouseRequest houseReq : houseRequests) {
                houses.add(houseRepository.findByUuid(houseReq.uuid())
                        .orElseThrow(() -> NotFoundException.of(House.class, houseReq.uuid())));
            }
        }

        person.setOwnerHouses(houses);
    }
}
