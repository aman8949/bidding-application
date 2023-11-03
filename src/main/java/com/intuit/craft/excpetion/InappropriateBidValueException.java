package com.intuit.craft.excpetion;

public class InappropriateBidValueException extends RuntimeException{

    public InappropriateBidValueException(String message) {
        super(message);
    }
    public InappropriateBidValueException() {
        super();
    }
}
