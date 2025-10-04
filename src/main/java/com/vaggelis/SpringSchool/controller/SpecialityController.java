package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.speciality.SpecialityInsertDTO;
import com.vaggelis.SpringSchool.dto.speciality.SpecialityReadDTO;
import com.vaggelis.SpringSchool.dto.speciality.SpecialityUpdateDTO;
import com.vaggelis.SpringSchool.exception.speciality.SpecialityAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.speciality.SpecialityNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Speciality;
import com.vaggelis.SpringSchool.service.speciality.ISpecialityService;
import com.vaggelis.SpringSchool.validator.speciality.SpecialityInsertValidator;
import com.vaggelis.SpringSchool.validator.speciality.SpecialityUpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/speciality")
public class SpecialityController {

    private final ISpecialityService specialityService;
    private final SpecialityInsertValidator insertValidator;
    private final SpecialityUpdateValidator updateValidator;

    // 🟩-----------------------------------------------
    //  Add a new speciality
    // 🟩-----------------------------------------------
    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Add a new speciality",
            description = """
        Creates a new speciality in the system.
        The request body must include a valid `SpecialityInsertDTO`.
        Returns the created speciality and its location URI.
        """,
            tags = {"Specialities"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Speciality created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpecialityReadDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or speciality already exists",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<SpecialityReadDTO> addSpeciality(
            @Parameter(description = "The speciality data to be added", required = true)
            @Valid @RequestBody SpecialityInsertDTO dto,
            BindingResult bindingResult) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        insertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Speciality speciality = specialityService.addSpeciality(dto);
            SpecialityReadDTO readDTO = Mapper.mapReadDtoSpeciality(speciality);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readDTO.getId())
                    .toUri();

            return ResponseEntity.created(location).body(readDTO);
        } catch (SpecialityAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }


    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Get a speciality by ID",
            description = """
        Retrieves the details of a specific speciality 
        based on its unique identifier (ID).
        """,
            tags = {"Specialities"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Speciality found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpecialityReadDTO.class))),
            @ApiResponse(responseCode = "404", description = "Speciality not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/speciality/{id}")
    public ResponseEntity<SpecialityReadDTO> getSpeciality(
            @Parameter(description = "The ID of the speciality to retrieve", required = true)
            @PathVariable Long id) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            Speciality speciality = specialityService.findSpecialityById(id);
            SpecialityReadDTO readDTO = Mapper.mapReadDtoSpeciality(speciality);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        } catch (SpecialityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }

    // 🟦-----------------------------------------------
    // 📘 Get all specialities
    // 🟦-----------------------------------------------
    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Get all specialities",
            description = """
        Retrieves a list of all available specialities 
        in the system.
        """,
            tags = {"Specialities"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of specialities retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SpecialityReadDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request or invalid data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/speciality/all")
    public ResponseEntity<List<SpecialityReadDTO>> getAllSpecialities() {
        // ---------------------------
        // Method logic starts here
        // ---------------------------
        try {
            List<Speciality> specialities = specialityService.findAllSpecialities();

            // Map entities to DTOs
            List<SpecialityReadDTO> readDTOS = new ArrayList<>();
            for (Speciality speciality : specialities) {
                readDTOS.add(Mapper.mapReadDtoSpeciality(speciality));
            }

            return new ResponseEntity<>(readDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }



    // 🟥-----------------------------------------------
    // ❌ Delete a speciality by ID
    // 🟥-----------------------------------------------
    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Delete a speciality by ID",
            description = """
        Deletes a speciality based on the provided ID.
        If the speciality does not exist, a 404 (Not Found)
        response is returned.
        """,
            tags = {"Specialities"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Speciality deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Speciality.class))),
            @ApiResponse(responseCode = "404", description = "Speciality not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/speciality/delete/{id}")
    public ResponseEntity<Speciality> deleteSpeciality(@PathVariable Long id) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------
        try {
            Speciality speciality = specialityService.deleteSpeciality(id);
            return ResponseEntity.ok(speciality);
        } catch (SpecialityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }


    // 🟩-----------------------------------------------
    // ✏️ Update an existing speciality
    // 🟩-----------------------------------------------
    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Update an existing speciality",
            description = """
        Updates the details of an existing speciality using the provided data.
        If validation fails, a 400 (Bad Request) is returned.
        If the speciality is not found, a 404 (Not Found) response is returned.
        """,
            tags = {"Specialities"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Speciality updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpecialityReadDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or validation failed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Speciality not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/speciality/update")
    public ResponseEntity<SpecialityReadDTO> updateSpeciality(
            @Valid @RequestBody SpecialityUpdateDTO dto,
            BindingResult bindingResult) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        // 🧩 Validate request data
        updateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            // 🛠️ Update entity and map to DTO
            Speciality speciality = specialityService.updateSpeciality(dto);
            SpecialityReadDTO readDTO = Mapper.mapReadDtoSpeciality(speciality);

            return new ResponseEntity<>(readDTO, HttpStatus.OK);

        } catch (SpecialityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }


}
