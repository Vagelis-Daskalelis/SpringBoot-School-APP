package com.vaggelis.SpringSchool.validator.course;


import com.vaggelis.SpringSchool.dto.course.CourseUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class CourseUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CourseUpdateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseUpdateDTO dto = (CourseUpdateDTO) target;

        // Validate name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name is required.");
        if (dto.getName() != null && dto.getName().length() > 20) {
            errors.rejectValue("name", "name.size", "Name must be at most 20 characters long.");
        }

        // Validate date
        if (dto.getDate() == null) {
            errors.rejectValue("date", "date.null", "Date is required.");
        } else if (dto.getDate().isBefore(LocalDate.now())) {
            errors.rejectValue("date", "date.past", "Date cannot be in the past.");
        }

        // Validate hours
        if (dto.getHours() <= 0) {
            errors.rejectValue("hours", "hours.invalid", "Hours must be greater than 0.");
        }
    }
}
