package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.request.PatchRequest;
import com.vaggelis.SpringSchool.dto.request.SignUpRequest;
import com.vaggelis.SpringSchool.dto.request.UpdateRequest;
import com.vaggelis.SpringSchool.dto.student.StudentReadDTO;
import com.vaggelis.SpringSchool.dto.teacher.TeacherReadDTO;
import com.vaggelis.SpringSchool.exception.student.StudentAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.service.crud.ICRUDService;
import com.vaggelis.SpringSchool.service.student.IStudentService;
import com.vaggelis.SpringSchool.service.teacher.ITeacherService;
import com.vaggelis.SpringSchool.validator.user.SignUpValidator;
import com.vaggelis.SpringSchool.validator.user.UpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final ICRUDService crudService;
    private final IStudentService studentService;
    private final ITeacherService teacherService;
    private final SignUpValidator signUpValidator;
    private final UpdateValidator updateValidator;
    private final PasswordEncoder passwordEncoder;



    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Creates a student (ADMIN, TEACHER)",
            description = "Registers a new student with the provided sign-up information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Student created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentReadDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or student already exists",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @PostMapping("/signUp/student")
    public ResponseEntity<StudentReadDTO> signUpStudent(
            @Parameter(
                    description = "Sign-up information for the student",
                    required = true
            )
            @Valid @RequestBody SignUpRequest request,
            BindingResult bindingResult) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        signUpValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Student student = studentService.studentSignUp(request);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readDTO.getId())
                    .toUri();

            return ResponseEntity.created(location).body(readDTO);
        } catch (StudentAlreadyExistsException e) {
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
            summary = "Find a student by ID (ADMIN, TEACHER)",
            description = "Retrieves a student by their ID and returns the student details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentReadDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @GetMapping("/student/{id}")
    public ResponseEntity<StudentReadDTO> findStudent(
            @Parameter(
                    description = "ID of the student to retrieve",
                    required = true
            )
            @PathVariable Long id) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            Student student = studentService.findStudentById(id);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
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
            summary = "Find a teacher by ID (ADMIN, TEACHER)",
            description = "Retrieves a teacher by their ID and returns the teacher details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeacherReadDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Teacher not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherReadDTO> findTeacher(
            @Parameter(
                    description = "ID of the teacher to retrieve",
                    required = true
            )
            @PathVariable Long id) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            Teacher teacher = teacherService.findTeacherById(id);
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
            summary = "Get all teachers (ADMIN, TEACHER)",
            description = "Retrieves a list of all teachers and returns their details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of teachers retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TeacherReadDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Failed to retrieve teachers",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @GetMapping("/teacher/all")
    public ResponseEntity<List<TeacherReadDTO>> findAllTeachers() {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            List<Teacher> teachers = teacherService.findAllTeachers();
            List<TeacherReadDTO> readDTOS = new ArrayList<>();
            for (Teacher teacher : teachers) {
                readDTOS.add(Mapper.mappingTeacherToReadDto(teacher));
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
            summary = "Get all students (ADMIN, TEACHER)",
            description = "Retrieves a list of all students and returns their details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of students retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = StudentReadDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Failed to retrieve students",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @GetMapping("/student/all")
    public ResponseEntity<List<StudentReadDTO>> findAllStudents() {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            List<Student> students = studentService.findAllStudents();
            List<StudentReadDTO> readDTOS = new ArrayList<>();
            for (Student student : students) {
                readDTOS.add(Mapper.mappingStudentToReadDto(student));
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
            summary = "Update student and user details (ADMIN, TEACHER)",
            description = "Updates the information of a student and their associated user account"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentReadDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @PutMapping("/update/student")
    public ResponseEntity<StudentReadDTO> updateStudentAndUser(
            @Parameter(
                    description = "Updated student information",
                    required = true
            )
            @Valid @RequestBody UpdateRequest request,
            BindingResult bindingResult) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        updateValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Student student = studentService.updateStudentAndUser(request);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
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
            summary = "Patch logged teacher details (ADMIN, TEACHER)",
            description = "Updates specific fields of a teacher using a patch request"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher patched successfully",
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
    @PutMapping("/patch/teacher")
    public ResponseEntity<TeacherReadDTO> updateStudent(
            @Parameter(
                    description = "Partial teacher data to update",
                    required = true
            )
            @Valid @RequestBody PatchRequest request,
            BindingResult bindingResult) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        updateValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Teacher teacher = teacherService.patchYourTeacher(request);
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
            summary = "Assign a course to a student (ADMIN, TEACHER)",
            description = "Adds a course to a specific student's list of courses by their IDs"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course successfully assigned to student",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadDTO.class))),
            @ApiResponse(responseCode = "404", description = "Student or course not found")
    })
    @PutMapping("/student/course/add/{studentId}/{courseId}")
    public ResponseEntity<StudentReadDTO> addCourseToStudent(
            @Parameter(description = "ID of the student", required = true, example = "1")
            @PathVariable Long studentId,

            @Parameter(description = "ID of the course to assign", required = true, example = "2")
            @PathVariable Long courseId
    ) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------
        try {
            Student student = studentService.addCourseToStudent(studentId, courseId);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);

            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
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
            summary = "Remove a course from a student (ADMIN, TEACHER)",
            description = "Removes the association of a course from a specific student by their IDs"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course successfully removed from student",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadDTO.class))),
            @ApiResponse(responseCode = "404", description = "Student or course not found")
    })
    @PutMapping("/student/course/remove/{studentId}/{courseId}")
    public ResponseEntity<StudentReadDTO> removeCourseFromStudent(
            @Parameter(description = "ID of the student", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "ID of the course to remove", required = true)
            @PathVariable Long courseId) {

        // ---------------------------
        // Method logic starts here
        // ---------------------------
        try {
            Student student = studentService.removeCourseFromStudent(studentId, courseId);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);

            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }

}
