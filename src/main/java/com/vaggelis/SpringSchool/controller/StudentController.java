package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.StudentReadDTO;
import com.vaggelis.SpringSchool.exception.StudentNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.service.ICRUDService;
import com.vaggelis.SpringSchool.validator.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/student")
@RequiredArgsConstructor
public class StudentController {

    private final ICRUDService crudService;
    private final SignUpValidator validator;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/string")
    public ResponseEntity<String> getMessage(){
        System.out.println("It works");
        return ResponseEntity.ok("It works");
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<StudentReadDTO> seeYourProfile(@PathVariable Long id){


        try {
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(crudService.seeYourProfile(id));
            return ResponseEntity.ok(readDTO);
        }catch (StudentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
