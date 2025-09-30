package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.PatchRequest;
import com.vaggelis.SpringSchool.dto.StudentReadDTO;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.service.student.IStudentService;
import com.vaggelis.SpringSchool.validator.SignUpValidator;
import com.vaggelis.SpringSchool.validator.UpdateValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


    @GetMapping("/profile/{id}")
    public ResponseEntity<StudentReadDTO> seeYourProfile(@PathVariable Long id){


        try {
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(studentService.seeYourProfile(id));
            return ResponseEntity.ok(readDTO);
        }catch (StudentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @PutMapping("/patch/student")
    public ResponseEntity<StudentReadDTO> updateStudent(@Valid @RequestBody PatchRequest request, BindingResult bindingResult){
        updateValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Student student = studentService.patchYourStudent(request);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
