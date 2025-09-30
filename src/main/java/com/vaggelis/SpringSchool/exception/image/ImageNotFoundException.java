package com.vaggelis.SpringSchool.exception.image;

public class ImageNotFoundException  extends Exception{

    public ImageNotFoundException(Class<?> entityClass, Long id){
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " not found");
    }
}
