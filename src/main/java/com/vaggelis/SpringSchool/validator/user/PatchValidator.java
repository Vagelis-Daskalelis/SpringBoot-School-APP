package com.vaggelis.SpringSchool.validator.user;

import com.vaggelis.SpringSchool.dto.request.PatchRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PatchValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PatchRequest.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        PatchRequest request = (PatchRequest) target;

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
    }
}
