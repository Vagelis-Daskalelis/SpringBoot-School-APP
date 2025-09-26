package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.exception.StudentAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.TeacherAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.UserAlreadyExistsException;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;

public interface IAuthenticationService {
    void studentSignUp(SignUpRequest request) throws StudentAlreadyExistsException;
    JWTAuthenticationResponse SignIn(SignInRequest request);
    void teacherSignUp(SignUpRequest request) throws TeacherAlreadyExistsException;
    public void logout(String email);
}
