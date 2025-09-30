package com.vaggelis.SpringSchool.exception.student;

public class StudentAlreadyExistsException extends Exception {

    public StudentAlreadyExistsException(Class<?> entityClass, String username) {
        super("Entity " + entityClass.getSimpleName() + " with username " + username + " already exists.");
    }
}
