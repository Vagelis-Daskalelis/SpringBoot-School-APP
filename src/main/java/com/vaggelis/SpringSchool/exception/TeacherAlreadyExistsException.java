package com.vaggelis.SpringSchool.exception;

public class TeacherAlreadyExistsException extends Exception {

    public TeacherAlreadyExistsException(Class<?> entityClass, String username) {
        super("Entity " + entityClass.getSimpleName() + " with username " + username + " already exists.");
    }
}
