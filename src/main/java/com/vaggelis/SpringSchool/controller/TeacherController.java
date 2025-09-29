package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.dto.StudentReadDTO;
import com.vaggelis.SpringSchool.dto.TeacherReadDTO;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.StudentAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.StudentNotFoundException;
import com.vaggelis.SpringSchool.exception.TeacherNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.service.ICRUDService;
import com.vaggelis.SpringSchool.validator.SignUpValidator;
import com.vaggelis.SpringSchool.validator.UpdateValidator;
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
@RequestMapping("api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final ICRUDService crudService;
    private final SignUpValidator signUpValidator;
    private final UpdateValidator updateValidator;
    private final PasswordEncoder passwordEncoder;


    //Creates a Student
    @PostMapping("/signUp/student")
    public ResponseEntity<StudentReadDTO> signUpStudent(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult){
        signUpValidator.validate(request, bindingResult);
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

    //Update a Student
    @PutMapping("/update/studentanduser")
    public ResponseEntity<StudentReadDTO> updateStudentAndUser(@Valid @RequestBody UpdateRequest request, BindingResult bindingResult){
        updateValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Student student = crudService.updateStudentAndUser(request);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/student")
    public ResponseEntity<StudentReadDTO> updateStudent(@Valid @RequestBody UpdateRequest request, BindingResult bindingResult){
        updateValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Student student = crudService.updateStudent(request);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Finds a student by the id
    @GetMapping("/student/{id}")
    public ResponseEntity<StudentReadDTO> findStudent(@PathVariable Long id){
        try {
            Student student = crudService.findStudentById(id);
            StudentReadDTO readDTO = Mapper.mappingStudentToReadDto(student);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Finds a teacher by the id
    @GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherReadDTO> findTeacher(@PathVariable Long id){
        try {
            Teacher teacher = crudService.findTeacherById(id);
            TeacherReadDTO readDTO = Mapper.mappingTeacherToReadDto(teacher);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        }catch (TeacherNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

}
