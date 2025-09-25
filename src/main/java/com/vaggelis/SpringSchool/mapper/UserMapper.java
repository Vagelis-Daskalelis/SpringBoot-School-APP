package com.vaggelis.SpringSchool.mapper;

import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;

public class UserMapper {

    private UserMapper(){}

//    public static Teacher extractAdminFromSignUpRequest(SignUpRequest request){
//        return new Teacher(null, "admin", "admin");
//    }

    public static Teacher extractTeacherFromSignUpRequest(SignUpRequest request){
        return new Teacher(null, request.getFirstname(), request.getLastname());
    }

    public static User extractUserFromTeacherSignUpRequest(SignUpRequest request){
        User user = User.getNewUserWithTeacherRole(request.getUname(), request.getPassword(), request.getEmail());
        return user;
    }

    public static User extractUserFromStudentSignUpRequest(SignUpRequest request){
        User user = User.getNewUserWithStudentRole(request.getUname(), request.getPassword(), request.getEmail());
        return user;
    }

}
