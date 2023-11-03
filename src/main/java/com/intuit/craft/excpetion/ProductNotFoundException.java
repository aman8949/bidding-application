package com.intuit.craft.excpetion;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException() {
        super();
    }
}
