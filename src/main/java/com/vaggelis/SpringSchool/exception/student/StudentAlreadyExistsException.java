package com.vaggelis.SpringSchool.exception.student;

public class StudentAlreadyExistsException extends Exception {

    public StudentAlreadyExistsException(Class<?> entityClass, String name) {
        super("Entity " + entityClass.getSimpleName() + " with name " + name + " already exists.");
    }
}
