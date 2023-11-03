package com.intuit.craft.service;

import com.intuit.craft.enums.Role;
import com.intuit.craft.excpetion.EntityNotCreatedException;
import com.intuit.craft.excpetion.InvalidInputException;
import com.intuit.craft.excpetion.UserNotFoundException;
import com.intuit.craft.model.User;
import com.intuit.craft.repository.UserRepository;
import com.intuit.craft.request.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {this.userRepository = userRepository;}
    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty())
            throw new UserNotFoundException("No Users Registered");
        return users;
    }

    @Override
    public User getUser(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())
            throw new UserNotFoundException("User not found");
        return user.get();
    }

    @Override
    public User addUser(UserRequestDto userObj) throws EntityNotCreatedException, InvalidInputException {
        log.info(userObj.toString());
        User user = null;
        try {
            user = User.builder().firstName(userObj.getFirstName()).lastName(userObj.getLastName()).emailId(userObj.getEmailId()).role(Role.valueOf(userObj.getRole())).build();
            return userRepository.saveAndFlush(user);
        }
        catch(IllegalArgumentException e){
            log.error("Invalid Role Provided");
            throw new InvalidInputException("Invalid Role Provided");
        }
        catch (DataAccessException e){
            log.error("Error while saving into database :{}", e.getMessage());
            throw new EntityNotCreatedException("Error while creating the user");
        }
    }
}
