package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.response.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.request.ResetPasswordRequest;
import com.vaggelis.SpringSchool.dto.request.SignInRequest;
import com.vaggelis.SpringSchool.service.crud.ICRUDService;
import com.vaggelis.SpringSchool.service.password.IPasswordResetService;
import com.vaggelis.SpringSchool.service.image.IImageService;
import com.vaggelis.SpringSchool.validator.user.SignUpValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final ICRUDService crudService;
    private final IPasswordResetService passwordResetService;



    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Sign in a user (ALL)",
            description = "Authenticates a user and returns a JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User signed in successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JWTAuthenticationResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthenticationResponse> signIn(
            @Parameter(
                    description = "Sign-in request containing username/email and password",
                    required = true
            )
            @RequestBody SignInRequest request) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        return ResponseEntity.ok(crudService.SignIn(request));
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }

    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Request password reset (ALL)",
            description = "Sends a password reset link to the specified email address"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset link sent successfully",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid email or request failed",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @PostMapping("/request-password-request")
    public ResponseEntity<String> requestReset(
            @Parameter(
                    description = "Email of the user requesting a password reset",
                    required = true
            )
            @RequestParam String email) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok("Reset link sent");
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }


    // ===========================
    // Swagger Documentation
    // ===========================
    @Operation(
            summary = "Reset password (ALL)",
            description = "Resets the user's password using a valid password reset token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password updated successfully",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid token or request failed",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Parameter(
                    description = "Password reset request containing token and new password",
                    required = true
            )
            @RequestBody ResetPasswordRequest req) {
        // ---------------------------
        // Method logic starts here
        // ---------------------------

        passwordResetService.resetPassword(req.getToken(), req.getNewPassword());
        return ResponseEntity.ok("Password updated");
        // ---------------------------
        // Method logic ends here
        // ---------------------------
    }

}
