package pl.kurzelakamil.bettingapp.authorizationserver.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

import pl.kurzelakamil.bettingapp.authorizationserver.UnitTests;
import pl.kurzelakamil.bettingapp.authorizationserver.notification.NotificationService;
import pl.kurzelakamil.bettingapp.authorizationserver.config.BeansConfig;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.CheckUserTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.HeaderDto;
import pl.kurzelakamil.bettingapp.authorizationserver.exception.UserNotFoundException;
import pl.kurzelakamil.bettingapp.authorizationserver.factory.UserFactory;
import pl.kurzelakamil.bettingapp.authorizationserver.model.Role;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;
import pl.kurzelakamil.bettingapp.authorizationserver.repository.UserRepository;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Category(UnitTests.class)
@RunWith(SpringRunner.class)
@Import(BeansConfig.class)
public class UserServiceTest {

    @Autowired
    private JavaMailSenderImpl mailSender;
    private UserRepository userRepository;
    private NotificationService notificationService;
    private UserService userService;
    private GreenMail greenMail;

    @Before
    public void setup(){
        userRepository = mock(UserRepository.class);
        notificationService = mock(NotificationService.class);
        userService = new UserService(mailSender, userRepository, notificationService);
    }

    @Test
    public void validateUser_reject(){
        CheckUserTransferObject checkUserTransferObject = checkUserTransferObject();
        User user = UserFactory.userNo1();

        when(userRepository.findByEmailOrUuid(checkUserTransferObject.getEmail(), checkUserTransferObject.getId())).thenReturn(Optional.of(user));

        userService.validateUser(checkUserTransferObject);

        verify(notificationService, times(1)).rejectUser(checkUserTransferObject.getId());
    }

    @Test
    public void validateUser_prepareUserInPendingStatus() throws MessagingException {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        mailSender.setPort(3025);
        mailSender.setHost("localhost");

        CheckUserTransferObject checkUserTransferObject = checkUserTransferObject();

        when(userRepository.findByEmailOrUuid(checkUserTransferObject.getEmail(), checkUserTransferObject.getId())).thenReturn(Optional.empty());

        userService.validateUser(checkUserTransferObject);

        Message[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("Account activation", messages[0].getSubject());
        assertEquals(checkUserTransferObject.getEmail(), messages[0].getRecipients(Message.RecipientType.TO)[0].toString());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User user = userArgumentCaptor.getValue();

        assertEquals(checkUserTransferObject.getId(), user.getUuid());
        assertNotNull(user.getVerificationToken());
        assertEquals(Role.RoleName.USER, user.getRole().getName());
        assertEquals(checkUserTransferObject.getEmail(), user.getEmail());
        assertEquals(User.UserStatus.PENDING, user.getStatus());

        greenMail.stop();
    }

    @Test
    public void activateUser_approveUser() {
        User user = UserFactory.userNo1();
        user.generateVerificationToken();

        when(userRepository.findByVerificationToken(user.getVerificationToken())).thenReturn(Optional.of(user));

        userService.activateUser(user.getVerificationToken());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(notificationService, times(1)).approveUser(userArgumentCaptor.capture());

        User approvedUser = userArgumentCaptor.getValue();

        assertEquals(User.UserStatus.ACTIVE,  approvedUser.getStatus());
    }

    @Test(expected = UserNotFoundException.class)
    public void activateUser_userNotFound(){
        User user = UserFactory.userNo1();
        user.generateVerificationToken();

        when(userRepository.findByVerificationToken(anyString())).thenReturn(Optional.empty());

        userService.activateUser(user.getVerificationToken());
    }

    private CheckUserTransferObject checkUserTransferObject(){
        return new CheckUserTransferObject(new HeaderDto("user-service", LocalDateTime
                .now()), 1L, "testEmail", "testHashedPassword");
    }

}
