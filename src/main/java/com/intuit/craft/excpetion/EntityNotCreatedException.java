package com.intuit.craft.excpetion;

public class EntityNotCreatedException extends RuntimeException{

    public EntityNotCreatedException(String message) {
        super(message);
    }
    public EntityNotCreatedException() {
        super();
    }
}
