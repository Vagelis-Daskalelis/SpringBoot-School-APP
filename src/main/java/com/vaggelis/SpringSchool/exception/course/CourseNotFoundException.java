package com.vaggelis.SpringSchool.exception.course;

public class CourseNotFoundException extends Exception{

    public CourseNotFoundException(Class<?> entityClass, Long id){
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " not found");
    }
}
