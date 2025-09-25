package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.exception.UserAlreadyExistsException;

public interface IAuthenticationService {
    void signUp(SignUpRequest request) throws UserAlreadyExistsException;
    JWTAuthenticationResponse SignIn(SignInRequest request);
    void managerSignUp(SignUpRequest request) throws UserAlreadyExistsException;
    public void logout(String email);
}
