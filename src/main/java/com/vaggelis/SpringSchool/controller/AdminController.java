package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.*;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherNotFoundException;
import com.vaggelis.SpringSchool.exception.user.UserNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.service.ICRUDService;
import com.vaggelis.SpringSchool.service.student.IStudentService;
import com.vaggelis.SpringSchool.service.teacher.ITeacherService;
import com.vaggelis.SpringSchool.validator.SignUpValidator;
import com.vaggelis.SpringSchool.validator.UpdateValidator;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ICRUDService crudService;
    private final IStudentService studentService;
    private final ITeacherService teacherService;
    private final SignUpValidator validator;
    private final UpdateValidator updateValidator;
    private final PasswordEncoder passwordEncoder;

    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Creates a teacher",
            description = "Registers a new teacher with the provided sign-up information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Teacher created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeacherReadDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or teacher already exists",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @PostMapping("/signUp/teacher")
    public ResponseEntity<TeacherReadDTO> signUpTeacher(
            @Parameter(
                    description = "Sign-up information for the teacher",
                    required = true
            )
            @Valid @RequestBody SignUpRequest request,
            BindingResult bindingResult) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Teacher teacher = teacherService.teacherSignUp(request);
            TeacherReadDTO readDTO = Mapper.mappingTeacherToReadDto(teacher);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readDTO.getId())
                    .toUri();

            return ResponseEntity.created(location).body(readDTO);
        } catch (TeacherAlreadyExistsException e) {
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
            summary = "Find a user by email",
            description = "Retrieves a user by their email address and returns user details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserReadDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @GetMapping("/user/{email}")
    public ResponseEntity<UserReadDTO> findUser(
            @Parameter(
                    description = "Email of the user to retrieve",
                    required = true
            )
            @PathVariable String email) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            User user = crudService.findUserByEmail(email);
            UserReadDTO readDTO = Mapper.mappingUserToReadDto(user);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        } catch (UserNotFoundException e) {
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
            summary = "Get all users",
            description = "Retrieves a list of all users and returns their details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of users retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UserReadDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Failed to retrieve users",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @GetMapping("/user/all")
    public ResponseEntity<List<UserReadDTO>> findAllUsers() {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            List<User> users = crudService.findAllUsers();
            List<UserReadDTO> readDTOS = new ArrayList<>();
            for (User user : users) {
                readDTOS.add(Mapper.mappingUserToReadDto(user));
            }
            return new ResponseEntity<>(readDTOS, HttpStatus.OK);
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
            summary = "Delete a teacher by ID",
            description = "Deletes a teacher from the system by their ID and returns the deleted teacher details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher deleted successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Teacher.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Teacher not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @DeleteMapping("/teacher/delete/{id}")
    public ResponseEntity<Teacher> deleteTeacher(
            @Parameter(
                    description = "ID of the teacher to delete",
                    required = true
            )
            @PathVariable Long id) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            Teacher teacher = teacherService.deleteTeacher(id);
            return ResponseEntity.ok(teacher);
        } catch (TeacherNotFoundException e) {
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
            summary = "Delete a student by ID",
            description = "Deletes a student from the system by their ID and returns the deleted student details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student deleted successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Student.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @DeleteMapping("/student/delete/{id}")
    public ResponseEntity<Student> deleteStudent(
            @Parameter(
                    description = "ID of the student to delete",
                    required = true
            )
            @PathVariable Long id) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            Student student = studentService.deleteStudent(id);
            return ResponseEntity.ok(student);
        } catch (StudentNotFoundException e) {
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
            summary = "Update teacher and user details",
            description = "Updates the information of a teacher and their associated user account"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeacherReadDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Teacher not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @PutMapping("/update/teacher")
    public ResponseEntity<TeacherReadDTO> updateStudentAndUser(
            @Parameter(
                    description = "Updated teacher information",
                    required = true
            )
            @Valid @RequestBody UpdateRequest request,
            BindingResult bindingResult) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        updateValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Teacher teacher = teacherService.updateTeacherAndUser(request);
            TeacherReadDTO readDTO = Mapper.mappingTeacherToReadDto(teacher);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        } catch (TeacherNotFoundException e) {
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
            summary = "Change user status",
            description = "Toggles a user's status between ACTIVE and BANNED"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User status changed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserReadDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    @PutMapping("status/{id}")
    public ResponseEntity<UserReadDTO> changeStatus(@PathVariable Long id){
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            User user = crudService.changeStatus(id);
            UserReadDTO readDTO = Mapper.mappingUserToReadDto(user);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }

}
