package com.vaggelis.SpringSchool.exception;

import com.vaggelis.SpringSchool.models.Student;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class StudentAlreadyExistsException extends Exception {

    public StudentAlreadyExistsException(Class<?> entityClass, String username) {
        super("Entity " + entityClass.getSimpleName() + " with username " + username + " already exists.");
    }
}
