package com.vaggelis.SpringSchool.exception.user;

public class UserAlreadyExistsException extends Exception{

    public UserAlreadyExistsException(String name){
        super("Entity already exists");
    }
}
