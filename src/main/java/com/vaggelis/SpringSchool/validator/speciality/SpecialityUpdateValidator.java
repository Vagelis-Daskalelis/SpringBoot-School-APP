package com.vaggelis.SpringSchool.validator.speciality;

import com.vaggelis.SpringSchool.dto.speciality.SpecialityUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SpecialityUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return SpecialityUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        SpecialityUpdateDTO dto = (SpecialityUpdateDTO) target;

        // Validate the name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name is required.");
        if (dto.getName() != null && (dto.getName().length() > 20)) {
            errors.rejectValue("name", "name.size", "Name must be between 3 and 20 characters.");
        }
    }
}
