package pl.kurzelakamil.bettingapp.authorizationserver.notification;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import pl.kurzelakamil.bettingapp.authorizationserver.UnitTests;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.CheckUserTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringRunner.class)
@Category(UnitTests.class)
@AutoConfigureTestDatabase
public class NotificationListenerTest {

    private NotificationListener notificationListener;

    @Autowired
    private NotificationChannel notificationChannel;

    @MockBean
    private UserService userService;

    @Before
    public void setup(){
        notificationListener = new NotificationListener(userService);
    }

    @Test
    public void checkUser() throws Exception {
        String messageInput = Files.readString(Paths.get("src/test/resources/json/CheckUserTransferObject.json"));

        notificationChannel.checkUser().send(new GenericMessage<>(messageInput));

        verify(userService, times(1)).validateUser(any(CheckUserTransferObject.class));
    }

    @Test(expected = MessageConversionException.class)
    public void checkUser_wrongPayload() {
        notificationChannel.checkUser().send(new GenericMessage<>("testString"));
    }
}
