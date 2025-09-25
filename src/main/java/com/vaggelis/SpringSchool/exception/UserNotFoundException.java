package com.vaggelis.SpringSchool.exception;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(Class<?> entityClass, Long id){
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " not found");
    }
}
