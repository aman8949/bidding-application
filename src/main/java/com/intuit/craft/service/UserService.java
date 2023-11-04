package com.intuit.craft.service;

import com.intuit.craft.enums.Role;
import com.intuit.craft.excpetion.EntityNotCreatedException;
import com.intuit.craft.excpetion.UserNotFoundException;
import com.intuit.craft.model.User;
import com.intuit.craft.request.BidRequestDto;
import com.intuit.craft.request.UserRequestDto;

import java.util.List;

public interface UserService {
    User getUser(Long userId) throws UserNotFoundException;
    List<User> getAllUsers() throws UserNotFoundException;
    User addUser(UserRequestDto user) throws EntityNotCreatedException;
    Boolean isRoleType(BidRequestDto bidRequestDto, Role role) throws UserNotFoundException;
}
