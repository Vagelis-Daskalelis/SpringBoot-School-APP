package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.PatchRequest;
import com.vaggelis.SpringSchool.dto.StudentReadDTO;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.service.student.IStudentService;
import com.vaggelis.SpringSchool.validator.PatchValidator;
import com.vaggelis.SpringSchool.validator.SignUpValidator;
import com.vaggelis.SpringSchool.validator.UpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("api/student")
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService studentService;
    private final SignUpValidator validator;
    private final UpdateValidator updateValidator;
    private final PasswordEncoder passwordEncoder;
    private final PatchValidator patchValidator;


    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "View student profile",
            description = "Retrieves the profile details of a student by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student profile retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StudentReadDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @GetMapping("/profile/{id}")
    public ResponseEntity<StudentReadDTO> seeYourProfile(
            @Parameter(
                    description = "ID of the student to view",
                    required = true
            )
            @PathVariable Long id) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        try {
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(studentService.seeYourProfile(id));
            return ResponseEntity.ok(readDTO);
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }

    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Patch student details",
            description = "Updates specific fields of a student using a patch request"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student patched successfully",
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
    @PutMapping("/patch/student")
    public ResponseEntity<StudentReadDTO> updateStudent(
            @Parameter(
                    description = "Partial student data to update",
                    required = true
            )
            @Valid @RequestBody PatchRequest request,
            BindingResult bindingResult) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        patchValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Student student = studentService.patchYourStudent(request);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        } catch (StudentNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }


//    @GetMapping("/profile")
//    public ResponseEntity<Student> getProfile() {
//        return ResponseEntity.ok(studentService.seeYourProfile());
//    }
}


/**
 *
 * @PutMapping("/user/your/update/{id}")
 *     public ResponseEntity<User> updateYourUser(@Valid @PathVariable Long id,
 *                                            @RequestBody UpdateRequest request,
 *                                            Authentication authentication,
 *                                                BindingResult bindingResult) {
 *         validator.validate(request, bindingResult);
 *         if (bindingResult.hasErrors()){
 *             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
 *         }
 *
 *         User currentUser = (User) authentication.getPrincipal();
 *         Long currentUserId = currentUser.getId();
 *
 *         try {
 *             User updatedUser = usersCrudService.updateYourUser(request, id, currentUserId);
 *             return new ResponseEntity<>(currentUser, HttpStatus.OK);
 *         } catch (UserNotFoundException e) {
 *             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
 *         }
 *     }
 *
 *     @DeleteMapping("/user/your/delete/{id}")
 *     public ResponseEntity<User> deleteYourUser(@PathVariable Long id,
 *                                            Authentication authentication){
 *         User currentUser = (User) authentication.getPrincipal();
 *         Long currentUserId = currentUser.getId();
 *
 *
 *         try {
 *             User deleted = usersCrudService.deleteYourUser(id, currentUserId);
 *             return ResponseEntity.ok(deleted);
 *         } catch (UserNotFoundException e) {
 *             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
 *         }
 *
 *     }
 */
