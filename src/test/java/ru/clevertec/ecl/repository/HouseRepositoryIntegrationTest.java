package ru.clevertec.ecl.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.ecl.config.TestSpringConfig;
import ru.clevertec.ecl.data.HouseTestBuilder;
import ru.clevertec.ecl.entity.House;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
public class HouseRepositoryIntegrationTest {

    @Autowired
    private HouseRepository houseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void findAllPaginationShouldReturnExpectedLimitOfHouses() {
        // given
        int limit = 3;
        int offset = 0;
        int expected = 3;

        // when
        List<House> houses = houseRepository.findAll(limit, offset);
        int actual = houses.size();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    public void findByIdShouldReturnExpectedHouse() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouse().getUuid();
        House expected = HouseTestBuilder.builder().build().buildHouse();

        // when
        House actual = houseRepository.findById(uuid).orElseThrow();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }

    @Test
    public void findHousesByPersonUuidShouldReturnExpectedHouse() {
        // given
        UUID uuid = UUID.fromString("03736b7f-3ca4-4af7-99ac-07628a7d8fe6");
        House expected = HouseTestBuilder.builder().build().buildHouse();

        // when
        House actual = houseRepository.findHousesByPersonUuid(uuid).get(0);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }

    @Test
    public void createShouldReturnExpectedHouse() {
        // given
        House house = HouseTestBuilder.builder().build().buildHouseForCreate();
        House expected = HouseTestBuilder.builder().build().buildHouseForCreate();

        // when
        House actual = houseRepository.create(house);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }

    @Test
    public void updateShouldReturnExpectedNumber() {
        // given
        House house = HouseTestBuilder.builder()
                .withNumber("11")
                .build().buildHouseForUpdate();
        House expected = HouseTestBuilder.builder()
                .withNumber("11")
                .build().buildHouseForUpdate();

        // when
        House actual = houseRepository.update(house);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }

    @Test
    public void deleteByIdShouldReturnExpectedUuid() {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouseForDelete().getUuid();
        House expected = HouseTestBuilder.builder().build().buildHouseForDelete();

        // when
        houseRepository.deleteById(uuid);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<House> criteriaQuery = criteriaBuilder.createQuery(House.class);
        Root<House> houseRoot = criteriaQuery.from(House.class);

        criteriaQuery.select(houseRoot).where(criteriaBuilder.equal(houseRoot.get("deleted"), true));

        House actual = entityManager.createQuery(criteriaQuery).getSingleResult();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid());
    }
}
