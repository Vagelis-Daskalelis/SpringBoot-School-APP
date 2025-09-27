package com.vaggelis.SpringSchool.exception;

public class StudentNotFoundException extends Exception{

    public StudentNotFoundException(Class<?> entityClass, Long id){
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " not found");
    }
}
