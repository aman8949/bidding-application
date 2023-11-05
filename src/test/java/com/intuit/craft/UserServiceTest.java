package com.intuit.craft;

import com.intuit.craft.enums.Role;
import com.intuit.craft.excpetion.UserNotFoundException;
import com.intuit.craft.model.User;
import com.intuit.craft.request.UserRequestDto;
import com.intuit.craft.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BiddingSystemApplication.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;


    @Test
    public void getUser_whenNoneIsThere(){
        assertThrows(UserNotFoundException.class, ()-> userService.getUser(10000L), "User Not Found");
    }
    @Test
    public void saveAndRetrieveUser_thenOK() {
        UserRequestDto userRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test@gmail.com").role(Role.BIDDER.toString()).build();
        User savedUser = userService.addUser(userRequestDto);
        User fetchedUser =  userService
                .getUser(savedUser.getId());
        System.out.println(savedUser);
        System.out.println(fetchedUser);

        assertNotNull(fetchedUser);
        assertEquals(savedUser, fetchedUser);
    }

}
