package com.vaggelis.SpringSchool.exception;

public class TeacherNotFoundException extends Exception{

    public TeacherNotFoundException(Class<?> entityClass, Long id){
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " not found");
    }
}
