package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.course.CourseInsertDTO;
import com.vaggelis.SpringSchool.dto.course.CourseReadDTO;
import com.vaggelis.SpringSchool.dto.course.CourseUpdateDTO;
import com.vaggelis.SpringSchool.exception.course.CourseNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Course;
import com.vaggelis.SpringSchool.service.course.ICourseService;
import com.vaggelis.SpringSchool.validator.course.CourseInsertValidator;
import com.vaggelis.SpringSchool.validator.course.CourseUpdateValidator;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

    private final ICourseService courseService;

    private final CourseInsertValidator insertValidator;
    private final CourseUpdateValidator updateValidator;


    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Add a new course (ADMIN)",
            description = "Creates a new course with the specified name, date, and duration in hours."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseReadDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation failed")
    })
    @PostMapping("/add")
    public ResponseEntity<CourseReadDTO> addCourse(
            @Parameter(description = "Course data to be added", required = true)
            @Valid @RequestBody CourseInsertDTO dto,
            BindingResult bindingResult) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------
        insertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Course course = courseService.addCourse(dto);
            CourseReadDTO readDTO = Mapper.mapReadDtoTOCourse(course);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readDTO.getId())
                    .toUri();

            return ResponseEntity.created(location).body(readDTO);
        } catch (Exception e) {
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
            summary = "Get all courses (ADMIN)",
            description = "Retrieves a list of all courses with their details, including name, date, and duration."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of courses",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CourseReadDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Failed to retrieve courses")
    })
    @GetMapping("/all")
    public ResponseEntity<List<CourseReadDTO>> findAllCourses() {
        // ---------------------------
        // Method logic starts here
        // ---------------------------
        List<Course> courses;

        try {
            courses = courseService.findAll();

            List<CourseReadDTO> readDTOS = courses.stream().map(Mapper::mapReadDtoTOCourse).toList();


            return new ResponseEntity<>(readDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }

    @Operation(
            summary = "Get a course by ID (ADMIN)",
            description = "Retrieves the details of a specific course using its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseReadDTO.class))),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/course/{id}")
    public ResponseEntity<CourseReadDTO> getCourse(
            @Parameter(description = "The ID of the course to retrieve", required = true)
            @PathVariable Long id) {

        // ---------------------------
        // Method logic starts here
        // ---------------------------
        try {
            Course course = courseService.findCourseById(id);
            CourseReadDTO readDTO = Mapper.mapReadDtoTOCourse(course);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        } catch (CourseNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }


    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Delete a course by ID (ADMIN)",
            description = "Deletes a specific course using its ID. Returns the deleted course if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("/course/delete/{id}")
    public ResponseEntity<Course> deleteCourse(
            @Parameter(description = "The ID of the course to delete", required = true)
            @PathVariable Long id) {

        // ---------------------------
        // Method logic starts here
        // ---------------------------
        try {
            Course course = courseService.deleteCourse(id);
            return ResponseEntity.ok(course);
        } catch (CourseNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }


    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Update an existing course (ADMIN)",
            description = "Updates a course based on the provided data. Returns the updated course if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseReadDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation errors occurred"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PutMapping("/speciality/update")
    public ResponseEntity<CourseReadDTO> updateCourse(
            @Parameter(description = "Course data to update", required = true)
            @Valid @RequestBody CourseUpdateDTO dto,
            BindingResult bindingResult) {

        // ---------------------------
        // Method logic starts here
        // ---------------------------
        updateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Course course = courseService.updateCourse(dto);
            CourseReadDTO readDTO = Mapper.mapReadDtoTOCourse(course);

            return new ResponseEntity<>(readDTO, HttpStatus.OK);

        } catch (CourseNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }
}
