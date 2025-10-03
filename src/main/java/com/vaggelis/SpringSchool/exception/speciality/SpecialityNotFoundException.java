package com.vaggelis.SpringSchool.exception.speciality;

public class SpecialityNotFoundException  extends Exception{

    public SpecialityNotFoundException(Class<?> entityClass, Long id){
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " not found");
    }
}
