package com.vaggelis.SpringSchool.exception.student;

public class StudentNotFoundException extends Exception{

    public StudentNotFoundException(Class<?> entityClass, Long id){
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " not found");
    }
}
