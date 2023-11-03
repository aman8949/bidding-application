package com.intuit.craft.utils;

import com.intuit.craft.excpetion.AuctionNotFoundException;
import com.intuit.craft.excpetion.ProductNotFoundException;
import com.intuit.craft.excpetion.UserNotCreatedException;
import com.intuit.craft.excpetion.UserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotCreatedException.class)
    protected ResponseEntity<Object> handleUserNotCreated(UserNotCreatedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    protected ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuctionNotFoundException.class)
    protected ResponseEntity<Object> handleAuctionNotFound(AuctionNotFoundException ex) {
        return new ResponseEntity<>("Bidding for the mentioned auction is not open or has already ended", HttpStatus.NOT_FOUND);
    }
}