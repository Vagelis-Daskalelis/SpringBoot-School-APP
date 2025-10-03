package com.vaggelis.SpringSchool.exception.speciality;

public class SpecialityAlreadyExistsException extends Exception{

    public SpecialityAlreadyExistsException(Class<?> entityClass, String username) {
        super("Entity " + entityClass.getSimpleName() + " with username " + username + " already exists.");
    }
}
