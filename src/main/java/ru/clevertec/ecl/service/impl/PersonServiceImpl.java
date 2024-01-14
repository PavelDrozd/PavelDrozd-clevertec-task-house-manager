package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.repository.PersonRepository;
import ru.clevertec.ecl.service.PersonService;
import ru.clevertec.ecl.validator.impl.PersonRequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Implementation of PersonService interface for process Person data objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    /** PersonRepository is used to get objects from repository module. */
    private final PersonRepository personRepository;

    /** HouseRepository is used to get objects from repository module. */
    private final HouseRepository houseRepository;

    /** PersonMapper for mapping DTO and entity Person objects. */
    private final PersonMapper mapper;

    /** PersonRequestValidator for validate accepted objects */
    private final PersonRequestValidator validator;

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
        validator.validate(personRequest);

        UUID houseId = personRequest.house().uuid();
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> NotFoundException.of(House.class, houseId));

        Person person = mapper.toPerson(personRequest);
        person.setHouse(house);

        Person created = personRepository.create(person);
        return mapper.toPersonResponse(created);
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
                .map(mapper::toPersonResponse)
                .toList();
    }

    /**
     * Get all Person entities from repository paginated with limit and offset, then return as PersonResponse.
     *
     * @param limit  expected integer value of limit.
     * @param offset expected integer value of offset.
     * @return List of PersonResponse objects.
     */
    @Override
    public List<PersonResponse> getAll(int limit, int offset) {
        log.debug("SERVICE: FIND ALL PERSONS WITH LIMIT: " + limit + " OFFSET: " + offset);
        return personRepository.findAll(limit, offset).stream()
                .map(mapper::toPersonResponse)
                .toList();
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
        return personRepository.findById(id)
                .map(mapper::toPersonResponse)
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
        validator.validate(personRequest);

        UUID id = personRequest.uuid();
        Person exist = personRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Person.class, id));

        if (isChanged(exist, personRequest)) {
            return getById(personRequest.uuid());
        }
        Person person = mergePerson(exist, personRequest);
        Person updated = personRepository.update(person);
        return getById(updated.getUuid());
    }

    /**
     * Delete Person entity from repository by UUID.
     *
     * @param id expected object type of UUID.
     */
    @Override
    public void deleteById(UUID id) {
        log.debug("SERVICE: DELETE PERSON BY UUID: " + id);
        personRepository.deleteById(id);
    }

    /**
     * Count all entities in repository and return it as Integer.
     *
     * @return Integer value of objects being counted.
     */
    @Override
    public int count() {
        log.debug("SERVICE: COUNT PERSONS.");
        return personRepository.count();
    }

    /**
     * Get residents of house from repository by House UUID.
     *
     * @param id expected object type of UUID.
     * @return List of PersonResponse objects.
     */
    @Override
    public List<PersonResponse> getPersonsByHouseUuid(UUID id) {
        log.debug("SERVICE: GET PERSONS BY HOUSE UUID: " + id);
        return personRepository.findPersonsByHouseUuid(id).stream()
                .map(mapper::toPersonResponse)
                .toList();
    }

    private boolean isChanged(Person exist, PersonRequest personRequest) {
        return exist.getName().equals(personRequest.name())
               && exist.getSurname().equals(personRequest.surname())
               && exist.getPassportSeries().equals(personRequest.passportSeries())
               && exist.getPassportNumber().equals(personRequest.passportNumber())
               && exist.getSex().equals(personRequest.sex())
               && personRequest.house() != null
               && exist.getHouse().getUuid().equals(personRequest.house().uuid());
    }

    private Person mergePerson(Person exist, PersonRequest personRequest) {
        Person person = mapper.toPerson(personRequest);
        person.setId(exist.getId());
        person.setCreateDate(exist.getCreateDate());

        setHouseToPerson(exist, personRequest, person);
        setHousesToPerson(exist, personRequest, person);

        return person;
    }

    private void setHouseToPerson(Person exist, PersonRequest personRequest, Person person) {
        HouseRequest houseRequest = personRequest.house();
        if (houseRequest == null) {
            person.setHouse(exist.getHouse());
        } else {
            House house = houseRepository.findById(houseRequest.uuid())
                    .orElseThrow(() -> NotFoundException.of(House.class, houseRequest.uuid()));
            person.setHouse(house);
        }
    }

    private void setHousesToPerson(Person exist, PersonRequest personRequest, Person person) {
        List<HouseRequest> houseRequests = personRequest.houses();
        List<House> houses = new ArrayList<>();
        if (houseRequests == null) {
            person.setHouses(exist.getHouses());
        } else {
            for (HouseRequest houseReq : houseRequests) {
                houses.add(houseRepository.findById(houseReq.uuid())
                        .orElseThrow(() -> NotFoundException.of(House.class, houseReq.uuid())));
            }
        }
        person.setHouses(houses);
    }
}
