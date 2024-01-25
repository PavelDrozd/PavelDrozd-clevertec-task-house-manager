package ru.clevertec.ecl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
                    name = "UUID",
                    schema = @Schema(
                            oneOf = UUID.class
                    ),
                    required = true,
                    description = "Universal unique identifier of object"
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
    @GetMapping("/{id}")
    public ResponseEntity<HouseResponse> get(@PathVariable UUID id) {
        HouseResponse houseResponse = houseService.getById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(houseResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get houses",
            description = "Get houses",
            parameters = @Parameter(
                    name = "Pageable",
                    schema = @Schema(
                            implementation = Pageable.class
                    ),
                    description = "Pageable object for pagination with size, page and sort parameters"
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
                            name = "UUID",
                            schema = @Schema(
                                    oneOf = UUID.class
                            ),
                            required = true,
                            description = "Universal unique identifier of object"
                    ),
                    @Parameter(
                            name = "Pageable",
                            schema = @Schema(
                                    implementation = Pageable.class
                            ),
                            description = "Pageable object for pagination with size, page and sort parameters"
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
    @GetMapping("/{id}/persons")
    public ResponseEntity<Page<PersonResponse>> getByHouse(@PathVariable UUID id, Pageable pageable) {
        Page<PersonResponse> personsResponse = personService.getPersonsByHouseUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personsResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get houses by matches",
            description = "Get houses when accepted parameter name match with house fields",
            parameters = {
                    @Parameter(
                            name = "UUID",
                            schema = @Schema(
                                    oneOf = UUID.class
                            ),
                            required = true,
                            description = "Universal unique identifier of object"
                    ),
                    @Parameter(
                            name = "Pageable",
                            schema = @Schema(
                                    implementation = Pageable.class
                            ),
                            description = "Pageable object for pagination with size, page and sort parameters"
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
                            name = "UUID",
                            schema = @Schema(
                                    oneOf = UUID.class
                            ),
                            required = true,
                            description = "Universal unique identifier of object"
                    ),
                    @Parameter(
                            name = "Pageable",
                            schema = @Schema(
                                    implementation = Pageable.class
                            ),
                            description = "Pageable object for pagination with size, page and sort parameters"
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
    @GetMapping("/{id}/tenants")
    public ResponseEntity<Page<PersonResponse>> getTenantsByHouse(@PathVariable UUID id, Pageable pageable) {
        Page<PersonResponse> personResponses = houseHistoryService.getTenantsByHouseUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @Operation(
            method = "GET",
            summary = "Get history of owners",
            description = "Get persons who own or have owned that house",
            parameters = {
                    @Parameter(
                            name = "UUID",
                            schema = @Schema(
                                    oneOf = UUID.class
                            ),
                            required = true,
                            description = "Universal unique identifier of object"
                    ),
                    @Parameter(
                            name = "Pageable",
                            schema = @Schema(
                                    implementation = Pageable.class
                            ),
                            description = "Pageable object for pagination with size, page and sort parameters"
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
    @GetMapping("/{id}/owners")
    public ResponseEntity<Page<PersonResponse>> getOwnersByHouse(@PathVariable UUID id, Pageable pageable) {
        Page<PersonResponse> personResponses = houseHistoryService.getOwnersByHouseUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @Operation(
            method = "POST",
            summary = "Create house",
            description = "Create house of accepted house request body",
            parameters = {
                    @Parameter(
                            name = "HouseRequest",
                            required = true,
                            schema = @Schema(
                                    anyOf = HouseRequest.class
                            ),
                            description = "Object type of house request"
                    )
            },
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
            parameters = {
                    @Parameter(
                            name = "HouseRequest",
                            required = true,
                            schema = @Schema(
                                    anyOf = HouseRequest.class
                            ),
                            description = "Object type of house request"
                    )
            },
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

            parameters = {
                    @Parameter(
                            name = "HouseRequest",
                            required = true,
                            schema = @Schema(
                                    anyOf = HouseRequest.class
                            ),
                            description = "Object type of house request"
                    )
            },
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
                    name = "UUID",
                    schema = @Schema(
                            oneOf = UUID.class
                    ),
                    required = true,
                    description = "Universal unique identifier of object"
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        houseService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
