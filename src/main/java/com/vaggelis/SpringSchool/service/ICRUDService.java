package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.teacher.TeacherAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherNotFoundException;
import com.vaggelis.SpringSchool.exception.user.UserNotFoundException;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;

import java.util.List;

public interface ICRUDService {
    JWTAuthenticationResponse SignIn(SignInRequest request);
    void logout(String email);
    User findUserByEmail(String email) throws UserNotFoundException;
    List<User> findAllUsers();
    User changeStatus(Long id) throws UserNotFoundException;
}
