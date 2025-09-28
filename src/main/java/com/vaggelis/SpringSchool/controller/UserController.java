package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.ResetPasswordRequest;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.service.ICRUDService;
import com.vaggelis.SpringSchool.service.IPasswordResetService;
import com.vaggelis.SpringSchool.validator.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final ICRUDService crudService;
    private final SignUpValidator validator;
    private final PasswordEncoder passwordEncoder;
    private final IPasswordResetService passwordResetService;


    @PostMapping("/signin")
    public ResponseEntity<JWTAuthenticationResponse> signIn(@RequestBody SignInRequest request){
        return ResponseEntity.ok(crudService.SignIn(request));
    }

    @PostMapping("/request-password-request")
    public ResponseEntity<String> requestReset(@RequestParam String email) {
        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok("Reset link sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
        passwordResetService.resetPassword(req.getToken(), req.getNewPassword());
        return ResponseEntity.ok("Password updated");
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        String email = authentication.getName(); // Gets the email from the JWT's subject
        crudService.logout(email);

        return ResponseEntity.ok("User logged out successfully");
    }
}
