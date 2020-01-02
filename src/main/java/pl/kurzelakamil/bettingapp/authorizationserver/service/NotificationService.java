package pl.kurzelakamil.bettingapp.authorizationserver.service;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserInputDto;
import pl.kurzelakamil.bettingapp.authorizationserver.api.SagaClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@EnableBinding(SagaClient.class)
public class NotificationService {

    private UserService userService;

    @StreamListener(SagaClient.USER_CREATED)
    protected void createUser(UserInputDto userInputDto){
        userService.validateCreatedUser(userInputDto);
    }
}
