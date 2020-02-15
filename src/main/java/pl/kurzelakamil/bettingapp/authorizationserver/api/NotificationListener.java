package pl.kurzelakamil.bettingapp.authorizationserver.api;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import pl.kurzelakamil.bettingapp.authorizationserver.dto.CheckUserTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@EnableBinding(NotificationChannel.class)
public class NotificationListener {

    private UserService userService;

    @StreamListener(NotificationChannel.CHECK_USER)
    protected void checkUser(CheckUserTransferObject transferObject){
        userService.validateUser(transferObject);
    }
}
