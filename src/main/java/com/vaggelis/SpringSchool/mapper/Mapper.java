package com.vaggelis.SpringSchool.mapper;

import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.dto.StudentReadDTO;
import com.vaggelis.SpringSchool.dto.TeacherReadDTO;
import com.vaggelis.SpringSchool.dto.UserReadDTO;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;


public class Mapper {


private Mapper() {} // prevent instantiation

    //Creates a Teacher from the signUp request
    public static Teacher extractTeacherFromSignUpRequest(SignUpRequest request) {
        return new Teacher(null, request.getFirstname(), request.getLastname());
    }

    //Creates a User with Teacher Role from the signUp request
    public static User extractUserWithTeacherRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return User.getNewUserWithTeacherRole(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
    }

    //Creates a Student from the signUp request
    public static Student extractStudentFromSignUpRequest(SignUpRequest request) {
        return new Student(null, request.getFirstname(), request.getLastname());
    }

    //Creates a User with Student Role from the signUp request
    public static User extractUserWithStudentRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return User.getNewUserWithStudentRole(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
    }


    //Maps User to UserReadDto
    public static UserReadDTO mappingUserToReadDto(User user){
        return new UserReadDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getStatus());
    }

    //Maps Teacher to TeacherReadDto
    public static TeacherReadDTO mappingTeacherToReadDto(Teacher teacher){
        return new TeacherReadDTO(teacher.getId(), teacher.getFirstname(), teacher.getLastname(), Mapper.mappingUserToReadDto(teacher.getUser()));
    }

    //Maps Student to StudentReadDto
    public static StudentReadDTO mappingStudentToReadDto(Student student){
        return new StudentReadDTO(student.getId(), student.getFirstname(), student.getLastname(), Mapper.mappingUserToReadDto(student.getUser()));
    }

}
