package ru.clevertec.ecl.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.repository.HouseRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HouseRepositoryImpl implements HouseRepository {

    private static final String FIND_ALL_HOUSES = "from House where deleted = false";
    private static final String COUNT_HOUSES = "SELECT count(*) FROM houses h WHERE h.deleted = false";

    @PersistenceContext
    private final EntityManager entityManager;


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
//        return entityManager.createQuery(FIND_ALL_HOUSES, House.class)
//                .getResultList();
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
    public Optional<House> findById(Long id) {
        log.debug("REPOSITORY: FIND HOUSE BY ID: " + id);
        House house = entityManager.find(House.class, id);
        System.out.println(house);
        return Optional.ofNullable(house);
    }

    @Override
    public House update(House house) {
        log.debug("REPOSITORY: UPDATE HOUSE: " + house);
        return entityManager.merge(house);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("REPOSITORY: DELETE HOUSE BY ID: " + id);
        entityManager.remove(id);
    }

    @Override
    public long count() {
        log.debug("REPOSITORY: COUNT HOUSES.");
        return entityManager.createNativeQuery(COUNT_HOUSES, Long.class)
                .getFirstResult();
    }
}
