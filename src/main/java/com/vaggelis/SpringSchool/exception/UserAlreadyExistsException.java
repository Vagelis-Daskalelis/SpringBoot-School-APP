package com.vaggelis.SpringSchool.exception;

public class UserAlreadyExistsException extends Exception{

    public UserAlreadyExistsException(String name){
        super("Entity already exists");
    }
}
