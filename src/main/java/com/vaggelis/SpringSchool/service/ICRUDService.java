package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.exception.*;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;

import java.util.List;

public interface ICRUDService {
    Student studentSignUp(SignUpRequest request) throws StudentAlreadyExistsException;
    JWTAuthenticationResponse SignIn(SignInRequest request);
    Teacher teacherSignUp(SignUpRequest request) throws TeacherAlreadyExistsException;
    public void logout(String email);
    User findUserByEmail(String email) throws UserNotFoundException;
    List<User> findAllUsers();
    Teacher findTeacherByLastname(String lastname) throws TeacherNotFoundException;
    List<Teacher> findAllTeachers();
    Student findStudentByLastname(String lastname) throws StudentNotFoundException;
    List<Student> findAllStudents();
    Teacher deleteTeacher(Long id) throws TeacherNotFoundException;
    Student deleteStudent(Long id) throws StudentNotFoundException;
}
