package ru.clevertec.ecl.integration.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.clevertec.ecl.config.DBContainerConfig;
import ru.clevertec.ecl.data.HouseTestBuilder;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.service.impl.HouseServiceImpl;
import ru.clevertec.ecl.util.DelayGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class HouseServiceImplIntegrationTest {

    private static final int THREAD_POOL = 6;

    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL);

    private final DelayGenerator delayGenerator = DelayGenerator.getRandomDelayGenerator(
            500, 2000, TimeUnit.MILLISECONDS);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse(DBContainerConfig.POSTGRES_CONTAINER_VERSION));

    @Autowired
    HouseServiceImpl houseService;

    @Test
    void check() throws Exception {
        // given
        UUID uuid = HouseTestBuilder.builder().build().buildHouse().getUuid();
        HouseRequest houseRequestForCreate = HouseTestBuilder.builder().build().buildHouseRequestForCreate();
        HouseRequest houseRequestForUpdate = HouseTestBuilder.builder().build().buildHouseRequestForUpdate();
        UUID uuidOfUpdatedHouse = houseRequestForUpdate.uuid();

        HouseResponse expectedGetHouseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        HouseResponse expectedCreateHouseResponse = HouseTestBuilder.builder().build().buildHouseResponseForCreate();
        HouseResponse expectedUpdateHouseResponse = HouseTestBuilder.builder().build().buildHouseResponseForUpdate();

        AtomicReference<HouseResponse> getHouseResponse = new AtomicReference<>();
        AtomicReference<HouseResponse> createHouseResponse = new AtomicReference<>();
        AtomicReference<HouseResponse> updateHouseResponse = new AtomicReference<>();
        AtomicReference<HouseResponse> getUpdatedHouseResponse = new AtomicReference<>();

        // when
        List<Callable<Void>> tasks = new ArrayList<>();
        tasks.add(() -> {
            getHouseResponse.set(houseService.getById(uuid));
            return null;
        });
        tasks.add(() -> {
            delayGenerator.delay();
            createHouseResponse.set(houseService.create(houseRequestForCreate));
            return null;
        });
        tasks.add(() -> {
            delayGenerator.delay();
            updateHouseResponse.set(houseService.update(houseRequestForUpdate));
            return null;
        });
        tasks.add(() -> {
            delayGenerator.delay();
            getUpdatedHouseResponse.set(houseService.getById(uuidOfUpdatedHouse));
            return null;
        });
        tasks.add(() -> {
            delayGenerator.delay();
            houseService.deleteById(uuid);
            return null;
        });

        List<Future<Void>> futures = executor.invokeAll(tasks);

        HouseResponse actualGetFirstHouseResponse = getHouseResponse.get();
        HouseResponse actualCreateHouseResponse = createHouseResponse.get();
        HouseResponse actualUpdateHouseResponse = updateHouseResponse.get();
        HouseResponse actualGetUpdatedHouseResponse = getUpdatedHouseResponse.get();

        // then
        assertThat(actualGetFirstHouseResponse)
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.uuid, expectedGetHouseResponse.uuid())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.country, expectedGetHouseResponse.country())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.area, expectedGetHouseResponse.area())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.city, expectedGetHouseResponse.city())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.street, expectedGetHouseResponse.street())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.number, expectedGetHouseResponse.number());

        assertThat(actualCreateHouseResponse)
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.country, expectedCreateHouseResponse.country())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.area, expectedCreateHouseResponse.area())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.city, expectedCreateHouseResponse.city())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.street, expectedCreateHouseResponse.street())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.number, expectedCreateHouseResponse.number());

        assertThat(actualUpdateHouseResponse)
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.uuid, expectedUpdateHouseResponse.uuid())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.country, expectedUpdateHouseResponse.country())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.area, expectedUpdateHouseResponse.area())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.city, expectedUpdateHouseResponse.city())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.street, expectedUpdateHouseResponse.street())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.number, expectedUpdateHouseResponse.number());

        assertThat(actualGetUpdatedHouseResponse)
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.uuid, expectedUpdateHouseResponse.uuid())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.country, expectedUpdateHouseResponse.country())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.area, expectedUpdateHouseResponse.area())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.city, expectedUpdateHouseResponse.city())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.street, expectedUpdateHouseResponse.street())
                .hasFieldOrPropertyWithValue(HouseResponse.Fields.number, expectedUpdateHouseResponse.number());
    }
}
