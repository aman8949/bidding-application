package com.intuit.craft.excpetion;

public class OperationNotAllowedException extends RuntimeException{

    public OperationNotAllowedException(String message) {
        super(message);
    }
    public OperationNotAllowedException() {
        super();
    }
}
