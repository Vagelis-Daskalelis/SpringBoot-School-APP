package com.vaggelis.SpringSchool.validator.user;

import com.vaggelis.SpringSchool.dto.request.SignUpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SignUpValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpRequest.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpRequest request = (SignUpRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "firstname.empty", "Firstname is required.");
        if (request.getFirstname() != null && (request.getFirstname().length() < 3 || request.getFirstname().length() > 20)) {
            errors.rejectValue("firstname", "firstname.size", "Firstname must be between 3 and 20 characters.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "lastname.empty", "Lastname is required.");
        if (request.getLastname() != null && (request.getLastname().length() < 3 || request.getLastname().length() > 20)) {
            errors.rejectValue("lastname", "lastname.size", "Lastname must be between 3 and 20 characters.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty", "Username is required.");
        if (request.getUsername() != null && (request.getUsername().length() < 3 || request.getUsername().length() > 20)) {
            errors.rejectValue("username", "username.size", "Username must be between 3 and 20 characters.");
        }

        // --- Password ---
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Password is required.");
        if (request.getPassword() != null && (request.getPassword().length() < 3 || request.getPassword().length() > 20)) {
            errors.rejectValue("password", "password.size", "Password must be between 3 and 20 characters.");
        }

        // --- Email ---
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", "Email is required.");
        if (request.getEmail() != null && (request.getEmail().length()<10 || request.getEmail().length() > 20)) {
            errors.rejectValue("email", "email.size", "Email must be between 10 and 20 characters.");
        }
    }
}
