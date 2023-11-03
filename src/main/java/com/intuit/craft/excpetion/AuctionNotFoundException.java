package com.intuit.craft.excpetion;

public class AuctionNotFoundException extends RuntimeException{

    public AuctionNotFoundException(String message) {
        super(message);
    }
    public AuctionNotFoundException() {
        super();
    }
}
