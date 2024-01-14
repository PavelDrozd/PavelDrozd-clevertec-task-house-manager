package ru.clevertec.ecl.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.entity.Person;
import ru.clevertec.ecl.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of PersonRepository interface for process House entities.
 * Using datasource for connect to the database.
 */
@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepository {

    /** SQL query for delete Person by set field deleted as true */
    private static final String DELETE_PERSON_BY_ID = "UPDATE persons p SET deleted = true WHERE p.uuid = ?";
    /** Select query for counting Person entities in database */
    private static final String COUNT_PERSONS = "SELECT count(*) AS total FROM persons p WHERE p.deleted = false";

    /** Entity manager for create queries for database */
    @PersistenceContext
    private final EntityManager entityManager;

    /** JDBC Template for create queries for database */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Create Person entity in database.
     *
     * @param person expected entity object type of Person.
     * @return Person object after create.
     */
    @Override
    public Person create(Person person) {
        log.debug("REPOSITORY: CREATE PERSON: " + person);
        entityManager.persist(person);
        return person;
    }

    /**
     * Find all Person entities in database.
     *
     * @return List of founded Person entities.
     */
    @Override
    public List<Person> findAll() {
        log.debug("REPOSITORY: FIND ALL PERSONS.");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        criteriaQuery.from(Person.class);

        return entityManager.createQuery(criteriaQuery)
                .getResultList();
    }

    /**
     * Find all Person entities in database.
     *
     * @param limit  expected integer value of limit.
     * @param offset expected integer value of offset.
     * @return List of founded Person entities.
     */
    @Override
    public List<Person> findAll(int limit, int offset) {
        log.debug("REPOSITORY: FIND ALL PERSONS WITH LIMIT: " + limit + " OFFSET: " + offset);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);

        Root<Person> root = criteriaQuery.from(Person.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("deleted"), false));

        return entityManager.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    /**
     * Find Person entity in database by UUID.
     *
     * @param id expected object type of UUID.
     * @return Optional of founded Person entity.
     */
    @Override
    public Optional<Person> findById(UUID id) {
        log.debug("REPOSITORY: FIND PERSON BY UUID: " + id);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);

        Root<Person> root = criteriaQuery.from(Person.class);
        criteriaQuery.where((criteriaBuilder.equal(root.get("uuid"), id)),
                criteriaBuilder.equal(root.get("deleted"), false));

        Person person = entityManager.createQuery(criteriaQuery).getSingleResult();
        return Optional.ofNullable(person);
    }

    /**
     * Update Person entity in database.
     *
     * @param person expected object type of Person for update.
     * @return updated House entity from database.
     */
    @Override
    public Person update(Person person) {
        log.debug("REPOSITORY: UPDATE PERSON: " + person);
        return entityManager.merge(person);
    }

    /**
     * Delete Person entity from database by UUID and set field deleted as true.
     *
     * @param id expected object type of UUID.
     */
    @Override
    public void deleteById(UUID id) {
        log.debug("REPOSITORY: DELETE PERSON BY UUID: " + id);
        jdbcTemplate.update(DELETE_PERSON_BY_ID, id);
    }

    /**
     * Count all Person entities in database.
     *
     * @return integer value of entities being counted.
     */
    @Override
    public int count() {
        log.debug("REPOSITORY: COUNT PERSONS.");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<Person> root = criteriaQuery.from(Person.class);
        criteriaQuery.select(criteriaBuilder.count(root))
                .where(criteriaBuilder.equal(root.get("deleted"), false));

        return Math.toIntExact(entityManager.createQuery(criteriaQuery).getSingleResult());
    }

    /**
     * Find persons from database by House UUID.
     *
     * @param id expected object type of UUID.
     * @return List of founded Person entities.
     */
    @Override
    public List<Person> findPersonsByHouseUuid(UUID id) {
        log.debug("REPOSITORY: FIND PERSONS BY HOUSE UUID: " + id);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        Root<House> houseRoot = criteriaQuery.from(House.class);

        Join<House, Person> housePersonJoin = houseRoot.join("residents", JoinType.INNER);
        criteriaQuery.select(housePersonJoin)
                .distinct(true)
                .where(criteriaBuilder.equal(houseRoot.get("uuid"), id));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
