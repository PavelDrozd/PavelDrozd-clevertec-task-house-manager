package ru.clevertec.ecl.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.repository.HouseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class HouseRepositoryImpl implements HouseRepository {

    private static final String FIND_ALL_HOUSES = "from House where deleted = false";
    private static final String DELETE_HOUSE_BY_ID = "UPDATE houses SET deleted = true WHERE id = ?";
    private static final String COUNT_HOUSES = "SELECT count(*) FROM houses h WHERE h.deleted = false";

    @PersistenceContext
    private final EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public House create(House house) {
        log.debug("REPOSITORY: CREATE HOUSE: " + house);
        entityManager.persist(house);
        return house;
    }

    @Override
    public List<House> findAll() {
        log.debug("REPOSITORY: FIND ALL HOUSES.");
        CriteriaQuery<House> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(House.class);
        criteriaQuery.from(House.class);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<House> findAll(int limit, int offset) {
        log.debug("REPOSITORY: FIND ALL HOUSES WITH LIMIT: " + limit + " OFFSET: " + offset);
        return entityManager.createQuery(FIND_ALL_HOUSES, House.class)//
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public Optional<House> findById(UUID id) {
        log.debug("REPOSITORY: FIND HOUSE BY ID: " + id);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<House> criteriaQuery = criteriaBuilder.createQuery(House.class);
        Root<House> root = criteriaQuery.from(House.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("uuid"), id));
        House house = entityManager.createQuery(criteriaQuery).getSingleResult();
        return Optional.ofNullable(house);
    }

    @Override
    public House update(House house) {
        log.debug("REPOSITORY: UPDATE HOUSE: " + house);
        return entityManager.merge(house);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("REPOSITORY: DELETE HOUSE BY ID: " + id);
        jdbcTemplate.update(DELETE_HOUSE_BY_ID, id);
    }

    @Override
    public long count() {
        log.debug("REPOSITORY: COUNT HOUSES.");
        return entityManager.createNativeQuery(COUNT_HOUSES, Long.class)
                .getFirstResult();
    }
}
