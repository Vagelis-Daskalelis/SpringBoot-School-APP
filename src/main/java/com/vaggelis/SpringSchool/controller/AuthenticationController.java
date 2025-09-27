package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.*;
import com.vaggelis.SpringSchool.exception.*;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.service.ICRUDService;
import com.vaggelis.SpringSchool.validator.SignUpValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/admin")
@RequiredArgsConstructor
public class AuthenticationController {

    private final ICRUDService crudService;
    private final SignUpValidator validator;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/string")
    public void getMessage(){
        System.out.println("It works");

    }

    //Creates a Teacher
    @PostMapping("/signUp/teacher")
    public ResponseEntity<TeacherReadDTO> signUpTeacher(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult){
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Teacher teacher = crudService.teacherSignUp(request);
            TeacherReadDTO readDTO = Mapper.mappingTeacherToReadDto(teacher);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readDTO.getId())
                    .toUri();

            return ResponseEntity.created(location).body(readDTO);
        }catch (TeacherAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Creates a Student
    @PostMapping("/signUp/student")
    public ResponseEntity<StudentReadDTO> signUpStudent(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult){
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Student student = crudService.studentSignUp(request);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readDTO.getId())
                    .toUri();

            return  ResponseEntity.created(location).body(readDTO);
        }catch (StudentAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/signin")
    public ResponseEntity<JWTAuthenticationResponse> signIn(@RequestBody SignInRequest request){
        return ResponseEntity.ok(crudService.SignIn(request));
    }


    //Finds a user by email
    @GetMapping("/user/{email}")
    public ResponseEntity<UserReadDTO> findUser(@PathVariable String email){
        try {
            User user = crudService.findUserByEmail(email);
            UserReadDTO readDTO = Mapper.mappingUserToReadDto(user);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Finds a teacher by the lastname
    @GetMapping("/teacher/{lastname}")
    public ResponseEntity<TeacherReadDTO> findTeacher(@PathVariable String lastname){
        try {
            Teacher teacher = crudService.findTeacherByLastname(lastname);
            TeacherReadDTO readDTO = Mapper.mappingTeacherToReadDto(teacher);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        }catch (TeacherNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Finds a student by the lastname
    @GetMapping("/student/{lastname}")
    public ResponseEntity<StudentReadDTO> findStudent(@PathVariable String lastname){
        try {
            Student student = crudService.findStudentByLastname(lastname);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Finds all users
    @GetMapping("/user/all")
    public ResponseEntity<List<UserReadDTO>> findAllUsers(){
        List<User> users;

        try {
            users = crudService.findAllUsers();
            List<UserReadDTO> readDTOS = new ArrayList<>();

            for (User user : users){
                readDTOS.add(Mapper.mappingUserToReadDto(user));
            }

            return new ResponseEntity<>(readDTOS, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Finds all teachers
    @GetMapping("/teacher/all")
    public ResponseEntity<List<TeacherReadDTO>> findAllTeachers(){
        List<Teacher> teachers;

        try {
            teachers = crudService.findAllTeachers();
            List<TeacherReadDTO> readDTOS = new ArrayList<>();

            for (Teacher teacher : teachers){
                readDTOS.add(Mapper.mappingTeacherToReadDto(teacher));
            }

            return new ResponseEntity<>(readDTOS, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Finds all students
    @GetMapping("/student/all")
    public ResponseEntity<List<StudentReadDTO>> findAllStudents(){
        List<Student> students;

        try {
            students = crudService.findAllStudents();
            List<StudentReadDTO> readDTOS = new ArrayList<>();

            for (Student student : students){
                readDTOS.add(Mapper.mappingStudentToReadDto(student));
            }

            return new ResponseEntity<>(readDTOS, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/teacher/delete/{id}")
    public ResponseEntity<Teacher> deleteTeacher(@PathVariable Long id){

        try {
            Teacher teacher = crudService.deleteTeacher(id);
            return ResponseEntity.ok(teacher);
        }catch (TeacherNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/student/delete/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id){

        try {
            Student student = crudService.deleteStudent(id);
            return ResponseEntity.ok(student);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
