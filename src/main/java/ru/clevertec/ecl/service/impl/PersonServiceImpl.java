package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.aspect.annotation.Create;
import ru.clevertec.ecl.aspect.annotation.Delete;
import ru.clevertec.ecl.aspect.annotation.Get;
import ru.clevertec.ecl.aspect.annotation.Logger;
import ru.clevertec.ecl.aspect.annotation.Update;
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.mapper.PersonMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.repository.PersonRepository;
import ru.clevertec.ecl.service.PersonService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Implementation of PersonService interface for process Person data objects.
 */
@Logger
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
     * Process PersonRequest object for create new Person entity and send it to a repository,
     * then create PersonResponse object.
     *
     * @param personRequest expected object of type PersonRequest.
     * @return created PersonResponse object.
     */
    @Create
    @Override
    @Transactional
    public PersonResponse create(PersonRequest personRequest) {
        UUID houseId = personRequest.tenantHouseUuidRequest();
        House house = houseRepository.findByUuidAndDeletedFalse(houseId)
                .orElseThrow(() -> NotFoundException.of(House.class, houseId));

        Person person = personMapper.toPerson(personRequest);
        person.setTenantHouse(house);

        Person created = personRepository.save(person);
        return personMapper.toPersonResponse(created);
    }

    /**
     * Get all Person entities from the repository paginated with limit and offset, then return as PersonResponse.
     *
     * @param pageable expected an object type of Pageable.
     * @return List of PersonResponse objects.
     */
    @Override
    public Page<PersonResponse> getAll(Pageable pageable) {
        return personRepository.findByDeletedFalse(pageable)
                .map(personMapper::toPersonResponse);
    }

    /**
     * Get Person from repository by UUID and return it as PersonResponse.
     *
     * @param uuid expected object type of UUID.
     * @return PersonResponse object.
     */
    @Get
    @Override
    public PersonResponse getByUuid(UUID uuid) {
        Optional<Person> person = personRepository.findByUuidAndDeletedFalse(uuid);

        if (person.isEmpty()) {
            throw NotFoundException.of(Person.class, uuid);
        }

        return person.map(personMapper::toPersonResponse)
                .orElseThrow();
    }

    /**
     * Update Person in repository by accept PersonRequest object with updated data and return as PersonResponse.
     *
     * @param personRequest expected an object type of PersonRequest with filled fields.
     * @return updated object PersonResponse.
     */
    @Update
    @Override
    @Transactional
    public PersonResponse update(PersonRequest personRequest) {
        UUID uuid = personRequest.uuid();
        Person exist = personRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> NotFoundException.of(Person.class, uuid));

        if (isChanged(exist, personRequest)) {
            return personMapper.toPersonResponse(exist);
        }

        Person person = mergeToUpdatePerson(exist, personRequest);
        Person updatedPerson = personRepository.save(person);

        return personMapper.toPersonResponse(updatedPerson);
    }

    /**
     * Update Person in repository by accepted PersonRequest object with updated part of data and
     * return as PersonResponse.
     *
     * @param personRequest expected an object type of PersonRequest with not fulfilled fields.
     * @return updated object PersonResponse.
     */
    @Update
    @Override
    @Transactional
    public PersonResponse updatePart(PersonRequest personRequest) {
        UUID uuid = personRequest.uuid();
        Person exist = personRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> NotFoundException.of(Person.class, uuid));

        if (isChanged(exist, personRequest)) {
            return personMapper.toPersonResponse(exist);
        }

        Person person = mergeToUpdatePartOfPerson(exist, personRequest);
        Person updatedPerson = personRepository.save(person);

        return personMapper.toPersonResponse(updatedPerson);
    }

    /**
     * Delete Person entity from repository by UUID.
     *
     * @param uuid expected object type of UUID.
     */
    @Delete
    @Override
    @Transactional
    public void deleteByUuid(UUID uuid) {
        Optional<Person> person = personRepository.findByUuidAndDeletedFalse(uuid);

        if (person.isPresent()) {
            Person personForDelete = person.get();
            personForDelete.setDeleted(true);

            personRepository.save(personForDelete);
        }
    }

    /**
     * Get residents of a house from repository by House UUID.
     *
     * @param uuid     expected object type of UUID.
     * @param pageable expected an object type of Pageable.
     * @return List of PersonResponse objects.
     */
    @Override
    public Page<PersonResponse> getPersonsByHouseUuid(UUID uuid, Pageable pageable) {
        return personRepository.findByOwnerHouses_UuidAndDeletedFalseAndOwnerHouses_DeletedFalse(uuid, pageable)
                .map(personMapper::toPersonResponse);
    }

    /**
     * Get persons from repository by any matches name.
     *
     * @param name     expected string name.
     * @param pageable expected an object type of Pageable.
     * @return List of PersonResponse objects.
     */
    @Override
    public Page<PersonResponse> getByNameMatches(String name, Pageable pageable) {
        return personRepository.findByNameMatches(name, pageable)
                .map(personMapper::toPersonResponse);
    }

    private boolean isChanged(Person exist, PersonRequest personRequest) {
        return personRequest.name() != null
               && !exist.getName().equals(personRequest.name())

               && personRequest.surname() != null
               && !exist.getSurname().equals(personRequest.surname())

               && personRequest.sex() != null
               && !exist.getSex().equals(personRequest.sex())

               && personRequest.passportSeries() != null
               && !exist.getPassportSeries().equals(personRequest.passportSeries())

               && personRequest.passportNumber() != null
               && !exist.getPassportNumber().equals(personRequest.passportNumber())

               && personRequest.tenantHouseUuidRequest() != null
               && !exist.getTenantHouse().getUuid().equals(personRequest.tenantHouseUuidRequest())

               && personRequest.ownerHousesUuidRequest() != null
               && isOwnerHousesUuidChanged(exist, personRequest);
    }

    private boolean isOwnerHousesUuidChanged(Person exist, PersonRequest personRequest) {
        List<UUID> ownerHousesUuidRequest = personRequest.ownerHousesUuidRequest();
        List<House> ownerHouses = exist.getOwnerHouses();

        long ownerHousesSize = ownerHouses.size();
        long ownerHousesRequestSize = ownerHousesUuidRequest.stream()
                .filter(uuid -> ownerHouses.stream()
                        .allMatch(house -> house.getUuid().equals(uuid))
                ).count();

        return ownerHousesRequestSize != ownerHousesSize;
    }

    private Person mergeToUpdatePerson(Person exist, PersonRequest personRequest) {
        Person person = personMapper.mergeWithNulls(exist, personRequest);

        person.setTenantHouse(getTenantHouseByPersonRequest(personRequest));

        if (personRequest.ownerHousesUuidRequest() != null) {
            person.setOwnerHouses(getOwnerHousesByPersonRequest(personRequest));
        }

        return person;
    }

    private Person mergeToUpdatePartOfPerson(Person exist, PersonRequest personRequest) {
        Person person = personMapper.merge(exist, personRequest);

        if (personRequest.tenantHouseUuidRequest() != null) {
            person.setTenantHouse(getTenantHouseByPersonRequest(personRequest));
        } else {
            person.setTenantHouse(exist.getTenantHouse());
        }

        if (personRequest.ownerHousesUuidRequest() != null) {
            person.setOwnerHouses(getOwnerHousesByPersonRequest(personRequest));
        } else {
            person.setOwnerHouses(exist.getOwnerHouses());
        }

        return person;

    }

    private House getTenantHouseByPersonRequest(PersonRequest personRequest) {
        UUID tenantHouseUuid = personRequest.tenantHouseUuidRequest();

        return houseRepository.findByUuidAndDeletedFalse(tenantHouseUuid)
                .orElseThrow(() -> NotFoundException.of(House.class, tenantHouseUuid));
    }

    private List<House> getOwnerHousesByPersonRequest(PersonRequest personRequest) {
        List<UUID> ownerHousesRequest = personRequest.ownerHousesUuidRequest();

        return ownerHousesRequest.stream()
                .map(houseRepository::findByUuidAndDeletedFalse)
                .map(Optional::orElseThrow)
                .collect(Collectors.toList());
    }
}
