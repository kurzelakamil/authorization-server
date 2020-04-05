package pl.kurzelakamil.bettingapp.authorizationserver.notification;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.TestSupportBinder;
import org.springframework.test.context.junit4.SpringRunner;

import pl.kurzelakamil.bettingapp.authorizationserver.UnitTests;
import pl.kurzelakamil.bettingapp.authorizationserver.factory.UserFactory;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringRunner.class)
@Category(UnitTests.class)
@AutoConfigureTestDatabase
public class NotificationServiceTest {

    private NotificationService notificationService;
    private NotificationChannel notificationChannel;

    @Autowired
    private TestSupportBinder testSupportBinder;

    @Before
    public void setup(){
        notificationChannel = Mockito.mock(NotificationChannel.class);
        notificationService = new NotificationService(notificationChannel);
    }

    @Test
    public void approveUser() {
        User user = UserFactory.userNo1();

        Mockito.when(notificationChannel.approveUser()).thenReturn(testSupportBinder.getChannelForName("approveUser"));
        notificationService.approveUser(user);

        verify(notificationChannel, times(1)).approveUser();
    }

    @Test
    public void rejectUser(){
        Mockito.when(notificationChannel.rejectUser()).thenReturn(testSupportBinder.getChannelForName("rejectUser"));
        notificationService.rejectUser(1L);

        verify(notificationChannel, times(1)).rejectUser();
    }
}
