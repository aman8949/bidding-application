package com.intuit.craft.excpetion;

public class UserNotCreatedException extends RuntimeException{

    public UserNotCreatedException(String message) {
        super(message);
    }
    public UserNotCreatedException() {
        super();
    }
}
