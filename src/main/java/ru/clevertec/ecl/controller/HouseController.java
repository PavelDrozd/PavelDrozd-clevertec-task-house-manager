package ru.clevertec.ecl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.data.request.HouseRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.service.HouseHistoryService;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.PersonService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
@Tag(name = "House")
public class HouseController {

    private final HouseService houseService;

    private final PersonService personService;

    private final HouseHistoryService houseHistoryService;

    @Operation(
            method = "GET",
            summary = "Get house by UUID",
            description = "Get house by accept UUID as path variable",
            parameters = @Parameter(
                    name = "uuid",
                    schema = @Schema(
                            oneOf = UUID.class
                    ),
                    required = true,
                    description = "Universal unique identifier of object",
                    example = "ac602dde-9556-4ef5-954c-aeebc42c5056"
            ),
            responses = {
                    @ApiResponse(
                            description = "Get house",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @GetMapping("/{uuid}")
    public ResponseEntity<HouseResponse> get(@PathVariable UUID uuid) {
        HouseResponse houseResponse = houseService.getByUuid(uuid);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houseResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get houses",
            description = "Get houses",
            parameters = @Parameter(
                    name = "pageable",
                    schema = @Schema(
                            implementation = Pageable.class
                    ),
                    description = "Pageable object for pagination with size, page and sort parameters",
                    example = """
                            {
                              "page": 0,
                              "size": 5,
                            }
                            """
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<HouseResponse>> getAll(Pageable pageable) {
        Page<HouseResponse> houses = houseService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houses);
    }

    @Operation(
            method = "GET",
            summary = "Get persons by House UUID",
            description = "Get persons who living in that house",
            parameters = {
                    @Parameter(
                            name = "uuid",
                            schema = @Schema(
                                    oneOf = UUID.class
                            ),
                            required = true,
                            description = "Universal unique identifier of object",
                            example = "a85a721b-c62b-428e-a777-3daa72fc5e3a"
                    ),
                    @Parameter(
                            name = "pageable",
                            schema = @Schema(
                                    implementation = Pageable.class
                            ),
                            description = "Pageable object for pagination with size, page and sort parameters",
                            example = """
                            {
                              "page": 0,
                              "size": 10,
                            }
                            """
                    ),
            },
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @GetMapping("/{uuid}/persons")
    public ResponseEntity<Page<PersonResponse>> getByHouse(@PathVariable UUID uuid, Pageable pageable) {
        Page<PersonResponse> personsResponse = personService.getPersonsByHouseUuid(uuid, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personsResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get houses by matches",
            description = "Get houses when accepted parameter name match with house fields",
            parameters = {
                    @Parameter(
                            name = "name",
                            schema = @Schema(
                                    oneOf = String.class
                            ),
                            required = true,
                            description = "Universal unique identifier of object",
                            example = "Чкалова"
                    ),
                    @Parameter(
                            name = "pageable",
                            schema = @Schema(
                                    implementation = Pageable.class
                            ),
                            description = "Pageable object for pagination with size, page and sort parameters",
                            example = """
                            {
                              "page": 0,
                              "size": 10,
                            }
                            """
                    ),
            },
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @GetMapping("/search/{name}")
    public ResponseEntity<Page<HouseResponse>> getByNameMatches(@PathVariable String name, Pageable pageable) {
        Page<HouseResponse> housesResponse = houseService.getByNameMatches(name, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(housesResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get history of tenants",
            description = "Get persons who living or have lived in that house",
            parameters = {
                    @Parameter(
                            name = "uuid",
                            schema = @Schema(
                                    oneOf = UUID.class
                            ),
                            required = true,
                            description = "Universal unique identifier of object",
                            example = "a85a721b-c62b-428e-a777-3daa72fc5e3a"
                    ),
                    @Parameter(
                            name = "pageable",
                            schema = @Schema(
                                    implementation = Pageable.class
                            ),
                            description = "Pageable object for pagination with size, page and sort parameters",
                            example = """
                            {
                              "page": 0,
                              "size": 10,
                            }
                            """
                    ),
            },
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @GetMapping("/{uuid}/tenants")
    public ResponseEntity<Page<PersonResponse>> getTenantsByHouse(@PathVariable UUID uuid, Pageable pageable) {
        Page<PersonResponse> personResponses = houseHistoryService.getTenantsByHouseUuid(uuid, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @Operation(
            method = "GET",
            summary = "Get history of owners",
            description = "Get persons who own or have owned that house",
            parameters = {
                    @Parameter(
                            name = "uuid",
                            schema = @Schema(
                                    oneOf = UUID.class
                            ),
                            required = true,
                            description = "Universal unique identifier of object",
                            example = "8126ee0f-edad-41fc-b845-41061439c652"
                    ),
                    @Parameter(
                            name = "pageable",
                            schema = @Schema(
                                    implementation = Pageable.class
                            ),
                            description = "Pageable object for pagination with size, page and sort parameters",
                            example = """
                            {
                              "page": 0,
                              "size": 10,
                            }
                            """
                    ),
            },
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @GetMapping("/{uuid}/owners")
    public ResponseEntity<Page<PersonResponse>> getOwnersByHouse(@PathVariable UUID uuid, Pageable pageable) {
        Page<PersonResponse> personResponses = houseHistoryService.getOwnersByHouseUuid(uuid, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @Operation(
            method = "POST",
            summary = "Create house",
            description = "Create house of accepted house request body",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Object type of house request",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                        "country": "Беларусь",
                                                        "area": "Витебская область",
                                                        "city": "Витебск",
                                                        "street": "Центральная",
                                                        "number": "1"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<HouseResponse> create(@RequestBody @Valid HouseRequest houseRequest) {
        HouseResponse houseResponse = houseService.create(houseRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(houseResponse);
    }

    @Operation(
            method = "PUT",
            summary = "Update house",
            description = "Update house of accepted house request body",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Object type of house request",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                        "uuid": "78cdcc8d-07df-496b-86aa-65aadd4cfc77",
                                                        "country": "Беларусь",
                                                        "area": "Могилевская область",
                                                        "city": "Могилев",
                                                        "street": "Главная",
                                                        "number": "222"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @PutMapping
    public ResponseEntity<HouseResponse> update(@RequestBody @Valid HouseRequest houseRequest) {
        HouseResponse houseResponse = houseService.update(houseRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houseResponse);
    }

    @Operation(
            method = "PATCH",
            summary = "Update part of house",
            description = "Update part of house of accepted house request body",

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Object type of house request",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                        "uuid": "ac602dde-9556-4ef5-954c-aeebc42c5056",
                                                        "number": "77"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @PatchMapping
    public ResponseEntity<HouseResponse> updatePart(@RequestBody @Valid HouseRequest houseRequest) {
        HouseResponse houseResponse = houseService.updatePart(houseRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houseResponse);
    }

    @Operation(
            method = "DELETE",
            summary = "Delete house by UUID",
            description = "Delete house by accept UUID as path variable",
            parameters = @Parameter(
                    name = "uuid",
                    schema = @Schema(
                            oneOf = UUID.class
                    ),
                    required = true,
                    description = "Universal unique identifier of object",
                    example = "b99d623b-4b7e-4c89-afce-2bed07ceb9fc"
            ),
            responses = {
                    @ApiResponse(
                            description = "Get house",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        houseService.deleteByUuid(uuid);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
