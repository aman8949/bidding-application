package com.intuit.craft.service.WinnerSchedulerServices;

import org.springframework.stereotype.Service;

@Service
public class EmailService implements NotificationService{

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }
}
