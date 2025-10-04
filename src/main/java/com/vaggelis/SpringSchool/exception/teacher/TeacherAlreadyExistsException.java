package com.vaggelis.SpringSchool.exception.teacher;

public class TeacherAlreadyExistsException extends Exception {

    public TeacherAlreadyExistsException(Class<?> entityClass, String name) {
        super("Entity " + entityClass.getSimpleName() + " with name " + name + " already exists.");
    }
}
