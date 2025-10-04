package com.vaggelis.SpringSchool.exception.speciality;

public class SpecialityAlreadyExistsException extends Exception{

    public SpecialityAlreadyExistsException(Class<?> entityClass, String name) {
        super("Entity " + entityClass.getSimpleName() + " with name " + name + " already exists.");
    }
}
