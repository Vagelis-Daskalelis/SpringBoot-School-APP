package com.vaggelis.SpringSchool.service.crud;

import com.vaggelis.SpringSchool.dto.response.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.request.SignInRequest;
import com.vaggelis.SpringSchool.exception.user.UserNotFoundException;
import com.vaggelis.SpringSchool.models.User;

import java.util.List;

public interface ICRUDService {
    JWTAuthenticationResponse SignIn(SignInRequest request);
    void logout(String email);
    User findUserByEmail(String email) throws UserNotFoundException;
    List<User> findAllUsers();
    User changeStatus(Long id) throws UserNotFoundException;
}
