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
import ru.clevertec.ecl.repository.HouseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of HouseRepository interface for process House entities.
 * Using datasource for connect to the database.
 */
@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class HouseRepositoryImpl implements HouseRepository {

    /** SQL query for delete Person by set field deleted as true */
    private static final String DELETE_HOUSE_BY_ID = "UPDATE houses h SET deleted = true WHERE h.uuid = ?";
    /** Select query for counting House entities in database */
    private static final String COUNT_HOUSES = "SELECT count(*) FROM houses h WHERE h.deleted = false";

    /** Entity manager for create queries for database */
    @PersistenceContext
    private final EntityManager entityManager;

    /** JDBC Template for create queries for database */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Create House entity in database.
     *
     * @param house expected entity object type of House.
     * @return House object after create.
     */
    @Override
    public House create(House house) {
        log.debug("REPOSITORY: CREATE HOUSE: " + house);

        entityManager.persist(house);

        return house;
    }

    /**
     * Find all House entities in database.
     *
     * @return List of founded House entities.
     */
    @Override
    public List<House> findAll() {
        log.debug("REPOSITORY: FIND ALL HOUSES.");

        CriteriaQuery<House> criteriaQuery = entityManager.getCriteriaBuilder()
                .createQuery(House.class);
        criteriaQuery.from(House.class);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Find all House entities in database.
     *
     * @param limit  expected integer value of limit.
     * @param offset expected integer value of offset.
     * @return List of founded House entities.
     */
    @Override
    public List<House> findAll(int limit, int offset) {
        log.debug("REPOSITORY: FIND ALL HOUSES WITH LIMIT: " + limit + " OFFSET: " + offset);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<House> criteriaQuery = criteriaBuilder.createQuery(House.class);

        Root<House> root = criteriaQuery.from(House.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("deleted"), false));

        return entityManager.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }


    /**
     * Find House entity in database by UUID.
     *
     * @param id expected object type of UUID.
     * @return Optional of founded House entity.
     */
    @Override
    public Optional<House> findById(UUID id) {
        log.debug("REPOSITORY: FIND HOUSE BY UUID: " + id);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<House> criteriaQuery = criteriaBuilder.createQuery(House.class);

        Root<House> root = criteriaQuery.from(House.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("uuid"), id),
                criteriaBuilder.equal(root.get("deleted"), false));

        House house = entityManager.createQuery(criteriaQuery)
                .getSingleResult();
        return Optional.ofNullable(house);
    }

    /**
     * Update House entity in database.
     *
     * @param house expected object type of House for update.
     * @return updated House entity from database.
     */
    @Override
    public House update(House house) {
        log.debug("REPOSITORY: UPDATE HOUSE: " + house);

        return entityManager.merge(house);
    }

    /**
     * Delete House entity from database by UUID and set field deleted as true.
     *
     * @param id expected object type of UUID.
     */
    @Override
    public void deleteById(UUID id) {
        log.debug("REPOSITORY: DELETE HOUSE BY ID: " + id);

        jdbcTemplate.update(DELETE_HOUSE_BY_ID, id);
    }

    /**
     * Count all House entities in database.
     *
     * @return integer value of entities being counted.
     */
    @Override
    public int count() {
        log.debug("REPOSITORY: COUNT HOUSES.");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<House> root = criteriaQuery.from(House.class);
        criteriaQuery.select(criteriaBuilder.count(root))
                .where(criteriaBuilder.equal(root.get("deleted"), false));

        return Math.toIntExact(entityManager.createQuery(criteriaQuery)
                .getSingleResult());
    }

    /**
     * Find houses from database by Person UUID.
     *
     * @param id expected object type of UUID.
     * @return List of founded House entities.
     */
    @Override
    public List<House> findHousesByPersonUuid(UUID id) {
        log.debug("REPOSITORY: FIND HOUSES BY PERSON UUID: " + id);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<House> criteriaQuery = criteriaBuilder.createQuery(House.class);
        Root<Person> personRoot = criteriaQuery.from(Person.class);

        Join<Person, House> ownersJoin = personRoot.join("houses", JoinType.INNER);
        criteriaQuery.select(ownersJoin)
                .distinct(true)
                .where(criteriaBuilder.equal(personRoot.get("uuid"), id));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
