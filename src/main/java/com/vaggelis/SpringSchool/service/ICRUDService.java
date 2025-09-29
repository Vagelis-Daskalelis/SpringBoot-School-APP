package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.*;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;

import java.util.List;

public interface ICRUDService {
    Student studentSignUp(SignUpRequest request) throws StudentAlreadyExistsException;
    JWTAuthenticationResponse SignIn(SignInRequest request);
    Teacher teacherSignUp(SignUpRequest request) throws TeacherAlreadyExistsException;
    void logout(String email);
    User findUserByEmail(String email) throws UserNotFoundException;
    List<User> findAllUsers();
    Teacher findTeacherById(Long id) throws TeacherNotFoundException;
    List<Teacher> findAllTeachers();
    Student findStudentById(Long id) throws StudentNotFoundException;
    List<Student> findAllStudents();
    Teacher deleteTeacher(Long id) throws TeacherNotFoundException;
    Student deleteStudent(Long id) throws StudentNotFoundException;
    Student seeYourProfile(Long targetId) throws StudentNotFoundException;
    Student updateStudentAndUser(UpdateRequest request) throws StudentNotFoundException;
    Student updateStudent(UpdateRequest request) throws StudentNotFoundException;
}
