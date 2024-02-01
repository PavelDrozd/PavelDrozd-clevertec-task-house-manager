package ru.clevertec.ecl.integration.service.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.data.HouseTestBuilder;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.entity.House;
import ru.clevertec.ecl.mapper.HouseMapper;
import ru.clevertec.ecl.repository.HouseRepository;
import ru.clevertec.ecl.service.impl.HouseServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class HouseServiceImplCacheIntegrationTest {

    private static final int THREAD_POOL = 6;

    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL);

    private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    @Autowired
    private HouseServiceImpl houseService;

    @MockBean
    private HouseRepository houseRepository;

    @MockBean
    private HouseMapper houseMapper;

    @Captor
    private ArgumentCaptor<House> houseCaptor;

    @SneakyThrows
    @Test
    void shouldThrowFindByUuidAndDeletedFalse2Times() {

        House houseFirst = HouseTestBuilder.builder().build().buildHouseFirst();
        House houseSecond = HouseTestBuilder.builder().build().buildHouseSecond();

        UUID uuidFirst = houseFirst.getUuid();
        UUID uuidSecond = houseSecond.getUuid();

        HouseResponse houseResponseFirst = HouseTestBuilder.builder().build().buildHouseResponseFirst();
        HouseResponse houseResponseSecond = HouseTestBuilder.builder().build().buildHouseResponseSecond();

        when(houseRepository.findByUuidAndDeletedFalse(uuidFirst)).thenReturn(Optional.of(houseFirst));
        when(houseRepository.findByUuidAndDeletedFalse(uuidSecond)).thenReturn(Optional.of(houseSecond));
        when(houseMapper.toHouseResponse(houseFirst)).thenReturn(houseResponseFirst);
        when(houseMapper.toHouseResponse(houseSecond)).thenReturn(houseResponseSecond);

        List<Callable<Void>> tasks = new ArrayList<>();
        tasks.add(() -> {
            houseService.getByUuid(uuidFirst);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(1000);
            houseService.getByUuid(uuidSecond);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(2000);
            houseService.getByUuid(uuidFirst);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(3000);
            houseService.getByUuid(uuidSecond);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(4000);
            houseService.getByUuid(uuidFirst);
            return null;
        });
        tasks.add(() -> {
            timeUnit.sleep(5000);
            houseService.getByUuid(uuidSecond);
            return null;
        });

        executor.invokeAll(tasks);

        verify(houseRepository, times(2)).findByUuidAndDeletedFalse(any(UUID.class));
    }

    @SneakyThrows
    @Test
    void shouldAssertAllHouseResponses() {
        // given
        House houseForGet = HouseTestBuilder.builder().build().buildHouse();
        House houseForCreate = HouseTestBuilder.builder().build().buildHouseForCreate();
        House houseForUpdate = HouseTestBuilder.builder().build().buildHouseForUpdate();
        House houseForDelete = HouseTestBuilder.builder().build().buildHouseForDelete();
        House houseWithDeletedTrue = HouseTestBuilder.builder()
                .withDeleted(true)
                .build().buildHouseForDelete();

        UUID uuidForGet = houseForGet.getUuid();
        UUID uuidForUpdate = houseForUpdate.getUuid();
        UUID uuidForDelete = houseForDelete.getUuid();
        HouseRequest houseRequestForCreate = HouseTestBuilder.builder().build().buildHouseRequestForCreate();
        HouseRequest houseRequestForUpdate = HouseTestBuilder.builder().build().buildHouseRequestForUpdate();

        HouseResponse houseResponseGet = HouseTestBuilder.builder().build().buildHouseResponse();
        HouseResponse houseResponseCreate = HouseTestBuilder.builder().build().buildHouseResponseForCreate();
        HouseResponse houseResponseUpdate = HouseTestBuilder.builder().build().buildHouseResponseForUpdate();

        HouseResponse expectedGetHouseResponse = HouseTestBuilder.builder().build().buildHouseResponse();
        HouseResponse expectedCreateHouseResponse = HouseTestBuilder.builder().build().buildHouseResponseForCreate();
        HouseResponse expectedUpdateHouseResponse = HouseTestBuilder.builder().build().buildHouseResponseForUpdate();

        when(houseRepository.findByUuidAndDeletedFalse(uuidForGet))
                .thenReturn(Optional.of(houseForGet));

        when(houseRepository.save(houseForCreate))
                .thenReturn(houseForCreate);

        when(houseRepository.findByUuidAndDeletedFalse(uuidForUpdate))
                .thenReturn(Optional.of(houseForUpdate));
        when(houseRepository.save(houseForUpdate))
                .thenReturn(houseForUpdate);

        when(houseRepository.findByUuidAndDeletedFalse(uuidForDelete))
                .thenReturn(Optional.of(houseForDelete));
        when(houseRepository.save(houseWithDeletedTrue))
                .thenReturn(houseWithDeletedTrue);

        when(houseMapper.toHouse(houseRequestForCreate))
                .thenReturn(houseForCreate);
        when(houseMapper.toHouse(houseRequestForUpdate))
                .thenReturn(houseForUpdate);

        when(houseMapper.toHouseResponse(houseForGet))
                .thenReturn(houseResponseGet);
        when(houseMapper.toHouseResponse(houseForCreate))
                .thenReturn(houseResponseCreate);
        when(houseMapper.toHouseResponse(houseForUpdate))
                .thenReturn(houseResponseUpdate);

        when(houseMapper.mergeWithNulls(houseForUpdate, houseRequestForUpdate))
                .thenReturn(houseForUpdate);

        // when
        Future<HouseResponse> getHouseResponse = executor.submit(() -> houseService.getByUuid(uuidForGet));
        Future<HouseResponse> createHouseResponse = executor.submit(() -> houseService.create(houseRequestForCreate));
        Future<HouseResponse> updateHouseResponse = executor.submit(() -> houseService.update(houseRequestForUpdate));
        executor.submit(() -> houseService.deleteByUuid(uuidForDelete));

        HouseResponse actualGetFirstHouseResponse = getHouseResponse.get();
        HouseResponse actualCreateHouseResponse = createHouseResponse.get();
        HouseResponse actualUpdateHouseResponse = updateHouseResponse.get();

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
    }
}
