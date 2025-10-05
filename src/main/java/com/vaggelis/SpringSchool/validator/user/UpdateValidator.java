package com.vaggelis.SpringSchool.validator.user;

import com.vaggelis.SpringSchool.dto.request.UpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UpdateValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateRequest.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateRequest request = (UpdateRequest) target;

        //Validate the firstname
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "firstname.empty", "Firstname is required.");
        if (request.getFirstname() != null && (request.getFirstname().length() < 3 || request.getFirstname().length() > 20)) {
            errors.rejectValue("firstname", "firstname.size", "Firstname must be between 3 and 20 characters.");
        }

        //Validate the lastname
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "lastname.empty", "Lastname is required.");
        if (request.getLastname() != null && (request.getLastname().length() < 3 || request.getLastname().length() > 20)) {
            errors.rejectValue("lastname", "lastname.size", "Lastname must be between 3 and 20 characters.");
        }

        //Validate the username
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty", "Username is required.");
        if (request.getUsername() != null && (request.getUsername().length() < 3 || request.getUsername().length() > 20)) {
            errors.rejectValue("username", "username.size", "Username must be between 3 and 20 characters.");
        }

        //Validate the password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Password is required.");
        if (request.getPassword() != null && (request.getPassword().length() < 3 || request.getPassword().length() > 20)) {
            errors.rejectValue("password", "password.size", "Password must be between 3 and 20 characters.");
        }

        //Validate the email
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", "Email is required.");
        if (request.getEmail() != null && (request.getEmail().length()<10 || request.getEmail().length() > 20)) {
            errors.rejectValue("email", "email.size", "Email must be between 10 and 20 characters.");
        }
    }
}

