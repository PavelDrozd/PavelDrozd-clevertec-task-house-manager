package ru.clevertec.ecl.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepository {

    private static final String FIND_ALL_PERSONS = "from Person where deleted = false";
    private static final String COUNT_PERSONS = "SELECT count(*) FROM persons p WHERE p.deleted = false";

    @PersistenceContext
    private final EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Person create(Person person) {
        log.debug("REPOSITORY: CREATE PERSON: " + person);
        entityManager.persist(person);
        return person;
    }

    @Override
    public List<Person> findAll() {
        log.debug("REPOSITORY: FIND ALL PERSONS.");
        return entityManager.createQuery(FIND_ALL_PERSONS, Person.class)
                .getResultList();
    }

    @Override
    public List<Person> findAll(int limit, int offset) {
        log.debug("REPOSITORY: FIND ALL PERSONS WITH LIMIT: " + limit + " OFFSET: " + offset);
        return entityManager.createQuery(FIND_ALL_PERSONS, Person.class)//
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public Optional<Person> findById(UUID id) {
        log.debug("REPOSITORY: FIND PERSON BY UUID: " + id);
        return Optional.ofNullable(
                entityManager.find(Person.class, id));
    }

    @Override
    public Person update(Person person) {
        log.debug("REPOSITORY: UPDATE PERSON: " + person);
        return entityManager.merge(person);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("REPOSITORY: DELETE PERSON BY UUID: " + id);
        entityManager.remove(id);
    }

    @Override
    public long count() {
        log.debug("REPOSITORY: COUNT PERSONS.");
        return entityManager.createNativeQuery(COUNT_PERSONS, Long.class)
                .getFirstResult();
    }
}
