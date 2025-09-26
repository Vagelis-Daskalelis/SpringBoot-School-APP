package com.vaggelis.SpringSchool.mapper;

import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


public class UserMapper {

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//
//
//    private UserMapper(){}
//
////    public static Teacher extractAdminFromSignUpRequest(SignUpRequest request){
////        return new Teacher(null, "admin", "admin");
////    }
//
//    public static Teacher extractTeacherFromSignUpRequest(SignUpRequest request){
//        return new Teacher(null, request.getFirstname(), request.getLastname());
//    }
//
//    public static User extractUserWithTeacherRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder){
//        User user = User.getNewUserWithTeacherRole(request.getUname(), passwordEncoder.encode(request.getPassword()), request.getEmail());
//        return user;
//    }
//
//    public static Student extractStudentFromSignUpRequest(SignUpRequest request){
//        return new Student(null, request.getFirstname(), request.getLastname());
//    }
//
//    public static User extractUserWithStudentRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder){
//        User user = User.getNewUserWithStudentRole(request.getUname(), passwordEncoder.encode(request.getPassword()), request.getEmail());
//        return user;
//    }

private UserMapper() {} // prevent instantiation

    public static Teacher extractTeacherFromSignUpRequest(SignUpRequest request) {
        return new Teacher(null, request.getFirstname(), request.getLastname());
    }

    public static User extractUserWithTeacherRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return User.getNewUserWithTeacherRole(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
    }

    public static Student extractStudentFromSignUpRequest(SignUpRequest request) {
        return new Student(null, request.getFirstname(), request.getLastname());
    }

    public static User extractUserWithStudentRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return User.getNewUserWithStudentRole(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
    }

}
