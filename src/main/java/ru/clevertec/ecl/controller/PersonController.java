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
import ru.clevertec.ecl.data.request.PersonRequest;
import ru.clevertec.ecl.data.response.HouseResponse;
import ru.clevertec.ecl.data.response.PersonResponse;
import ru.clevertec.ecl.service.HouseHistoryService;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.PersonService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
@Tag(name = "Person")
public class PersonController {

    private final PersonService personService;

    private final HouseService houseService;

    private final HouseHistoryService houseHistoryService;

    @Operation(
            method = "GET",
            summary = "Get person by UUID",
            description = "Get person by accept UUID as path variable",
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
    public ResponseEntity<PersonResponse> get(@PathVariable UUID id) {
        PersonResponse personResponse = personService.getById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get persons",
            description = "Get persons",
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
    public ResponseEntity<Page<PersonResponse>> getAll(Pageable pageable) {
        Page<PersonResponse> persons = personService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(persons);
    }

    @Operation(
            method = "GET",
            summary = "Get houses by person UUID",
            description = "Get houses who own by person",
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
    @GetMapping("/{id}/houses")
    public ResponseEntity<Page<HouseResponse>> getByPerson(@PathVariable UUID id, Pageable pageable) {
        Page<HouseResponse> housesResponse = houseService.getHousesByPersonUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(housesResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get persons by matches",
            description = "Get persons when accepted parameter name match with house fields",
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
    public ResponseEntity<Page<PersonResponse>> getByNameMatches(@PathVariable String name, Pageable pageable) {
        Page<PersonResponse> personsResponse = personService.getByNameMatches(name, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personsResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get history of houses by tenant",
            description = "Get houses where that person live or have lived",
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
    public ResponseEntity<Page<HouseResponse>> getHousesByTenant(@PathVariable UUID id, Pageable pageable) {
        Page<HouseResponse> personResponses = houseHistoryService.getHousesByTenantUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @Operation(
            method = "GET",
            summary = "Get history of houses by owner",
            description = "Get houses where that person own or have owned",
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
    public ResponseEntity<Page<HouseResponse>> getHousesByOwner(@PathVariable UUID id, Pageable pageable) {
        Page<HouseResponse> personResponses = houseHistoryService.getHousesByOwnerUuid(id, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponses);
    }

    @Operation(
            method = "POST",
            summary = "Create person",
            description = "Create person of accepted person request body",
            parameters = {
                    @Parameter(
                            name = "PersonRequest",
                            required = true,
                            schema = @Schema(
                                    anyOf = PersonRequest.class
                            ),
                            description = "Object type of person request"
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
    public ResponseEntity<PersonResponse> create(@RequestBody @Valid PersonRequest personRequest) {
        PersonResponse personResponse = personService.create(personRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personResponse);
    }

    @Operation(
            method = "PUT",
            summary = "Update person",
            description = "Update person of accepted person request body",
            parameters = {
                    @Parameter(
                            name = "PersonRequest",
                            required = true,
                            schema = @Schema(
                                    anyOf = PersonRequest.class
                            ),
                            description = "Object type of person request"
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
    public ResponseEntity<PersonResponse> update(@RequestBody @Valid PersonRequest personRequest) {
        PersonResponse personResponse = personService.update(personRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponse);
    }

    @Operation(
            method = "PATCH",
            summary = "Update part of person",
            description = "Update part of person of accepted person request body",
            parameters = {
                    @Parameter(
                            name = "PersonRequest",
                            required = true,
                            schema = @Schema(
                                    anyOf = PersonRequest.class
                            ),
                            description = "Object type of person request"
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
    public ResponseEntity<PersonResponse> updatePart(@RequestBody @Valid PersonRequest personRequest) {
        PersonResponse personResponse = personService.updatePart(personRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(personResponse);
    }

    @Operation(
            method = "DELETE",
            summary = "Delete person by UUID",
            description = "Delete person by accept UUID as path variable",
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
        personService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
