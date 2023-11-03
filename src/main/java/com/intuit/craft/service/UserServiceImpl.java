package com.intuit.craft.service;

import com.intuit.craft.enums.Role;
import com.intuit.craft.excpetion.UserNotCreatedException;
import com.intuit.craft.excpetion.UserNotFoundException;
import com.intuit.craft.model.User;
import com.intuit.craft.repository.UserRepository;
import com.intuit.craft.request.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
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
            throw new UserNotFoundException();
        return user.get();
    }

    @Override
    public User addUser(UserRequestDto userObj) throws UserNotCreatedException {
        log.info(userObj.toString());
        User user = null;
        try {
            user = User.builder().firstName(userObj.getFirstName()).lastName(userObj.getLastName()).emailId(userObj.getEmailId()).role(Role.valueOf(userObj.getRole())).build();
            userRepository.saveAndFlush(user);
        }
        catch(IllegalArgumentException e){
            log.error("Invalid Role or emailId Provided");
            throw new UserNotCreatedException("Invalid Role or emailId Provided");
        }
        catch (DataAccessException e){
            log.error("Error while saving into database :{}", e.getMessage());
            throw new UserNotCreatedException("Error while creating the user");
        }
        return user;
    }
}
