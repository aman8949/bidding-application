package com.intuit.craft.controller;

import com.intuit.craft.model.User;
import com.intuit.craft.request.UserRequestDto;
import com.intuit.craft.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    UserController(UserService userService)
    {
        this.userService = userService;
    }

    @Operation(summary = "Get User by userId", description = "userId should be valid")
    @GetMapping("/{id}")
    public ResponseEntity<User> userDetails(@PathVariable("id") Long userId)
    {
        // fetch details from user table and return
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @Operation(summary = "get all users")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUser()
    {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Create new user")
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequestDto user)
    {
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }
}
