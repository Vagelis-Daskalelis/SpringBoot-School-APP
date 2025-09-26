package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.exception.StudentAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.TeacherAlreadyExistsException;
import com.vaggelis.SpringSchool.service.IAuthenticationService;
import com.vaggelis.SpringSchool.validator.SignUpValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/admin")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationService authenticationService;
    private final SignUpValidator validator;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/string")
    public void getMessage(){
        System.out.println("It works");

    }

    @PostMapping("/signUp/teacher")
    public ResponseEntity<JWTAuthenticationResponse> signUpTeacher(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult){
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            authenticationService.teacherSignUp(request);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (TeacherAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup/student")
    public ResponseEntity<JWTAuthenticationResponse> signUpStudent(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult){
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            authenticationService.studentSignUp(request);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (StudentAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthenticationResponse> signIn(@RequestBody SignInRequest request){
        return ResponseEntity.ok(authenticationService.SignIn(request));
    }
}
