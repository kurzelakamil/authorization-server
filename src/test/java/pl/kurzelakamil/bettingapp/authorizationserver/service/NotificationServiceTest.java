package pl.kurzelakamil.bettingapp.authorizationserver.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.boot.test.context.SpringBootTest;

import pl.kurzelakamil.bettingapp.authorizationserver.UnitTests;
import pl.kurzelakamil.bettingapp.authorizationserver.factory.UserInputDtoFactory;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserInputDto;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Category(UnitTests.class)
public class NotificationServiceTest {

    UserService userService;
    NotificationService notificationService;

    @Before
    public void setup(){
        userService = mock(UserService.class);
        notificationService = new NotificationService(userService);
    }

    @Test
    public void createUser(){
        UserInputDto userInputDto = UserInputDtoFactory.userInputDtoNo1();
        notificationService.createUser(userInputDto);
        verify(userService, times(1)).validateCreatedUser(userInputDto);
    }
}
