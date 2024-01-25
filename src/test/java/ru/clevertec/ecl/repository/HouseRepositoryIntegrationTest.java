package ru.clevertec.ecl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.clevertec.ecl.config.DBContainerConfig;
import ru.clevertec.ecl.data.HouseTestBuilder;
import ru.clevertec.ecl.entity.House;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class HouseRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse(DBContainerConfig.POSTGRES_CONTAINER_VERSION));

    @Autowired
    HouseRepository houseRepository;

    @Test
    public void findAllPaginationShouldReturnExpectedLimitOfHouses() {
        // given
        int pageSize = 3;
        Pageable pageable = Pageable.ofSize(pageSize);
        int expected = 3;

        // when
        Page<House> houses = houseRepository.findByDeletedFalse(pageable);
        int actual = houses.getSize();

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
        House actual = houseRepository.findByUuidAndDeletedFalse(uuid).orElseThrow();

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
        Pageable pageable = Pageable.unpaged();
        House expected = HouseTestBuilder.builder()
                .withUuid(UUID.fromString("061783b1-c63b-4fb2-a9d0-9d90842911a2"))
                .withCountry("Беларусь")
                .withArea("Минская область")
                .withCity("Солигорск")
                .withStreet("К.Заслонова")
                .withNumber("52")
                .build().buildHouse();

        // when
        House actual = houseRepository.findByTenants_UuidAndDeletedFalseAndTenants_DeletedFalse(uuid, pageable)
                .getContent()
                .get(0);

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
        House actual = houseRepository.save(house);

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
        House actual = houseRepository.save(house);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }
}
