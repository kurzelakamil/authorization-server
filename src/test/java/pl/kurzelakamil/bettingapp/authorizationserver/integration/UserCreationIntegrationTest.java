package pl.kurzelakamil.bettingapp.authorizationserver.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.mail.Message;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.TestSupportBinder;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import pl.kurzelakamil.bettingapp.authorizationserver.IntegrationTests;
import pl.kurzelakamil.bettingapp.authorizationserver.notification.NotificationChannel;
import pl.kurzelakamil.bettingapp.authorizationserver.notification.NotificationListener;
import pl.kurzelakamil.bettingapp.authorizationserver.notification.NotificationService;
import pl.kurzelakamil.bettingapp.authorizationserver.exception.UserNotFoundException;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;
import pl.kurzelakamil.bettingapp.authorizationserver.repository.UserRepository;
import pl.kurzelakamil.bettingapp.authorizationserver.service.UserService;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Category(IntegrationTests.class)
public class UserCreationIntegrationTest {

    private NotificationListener notificationListener;
    private UserService userService;
    private NotificationService notificationService;
    private GreenMail greenMail;

    @Autowired
    private NotificationChannel notificationChannel;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestSupportBinder testSupportBinder;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setup(){
        notificationService = new NotificationService(notificationChannel);
        userService = new UserService(mailSender, userRepository, notificationService);
        notificationListener = new NotificationListener(userService);
    }

    @Test
    public void createUserProcessIntegrationTest_reject() throws IOException, JSONException, InterruptedException {
        String messageInput = Files.readString(Paths.get("src/test/resources/json/CheckUserTransferObjectIntegrationExisted.json"));

        notificationChannel.checkUser().send(new GenericMessage<>(messageInput));

        GenericMessage<String> message = (GenericMessage<String>) testSupportBinder.messageCollector().forChannel(notificationChannel.rejectUser()).take();

        String payload = message.getPayload();
        String expected = "{\"header\":{\"sender\":\"authorization-server\"},\"id\":2,\"rejectionReason\":\"USER_ALREADY_EXISTS\"}";

        JSONAssert.assertEquals(expected, payload, JSONCompareMode.LENIENT);
    }

    @Test
    public void createUserProcessIntegrationTest_approve() throws Exception {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        mailSender.setPort(3025);
        mailSender.setHost("localhost");

        String messageInput = Files.readString(Paths.get("src/test/resources/json/CheckUserTransferObjectIntegrationNotExisted.json"));

        notificationChannel.checkUser().send(new GenericMessage<>(messageInput));

        User createdUser = userRepository.findByEmail("testEmail999").orElseThrow(() -> UserNotFoundException.emailNotFound("testEmail999"));
        assertEquals(999L, createdUser.getUuid().longValue());
        assertEquals("testEmail999", createdUser.getEmail());
        assertEquals(User.UserStatus.PENDING, createdUser.getStatus());
        assertNotNull(createdUser.getVerificationToken());
        assertNotNull(createdUser.getPassword());

        Message[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("Account activation", messages[0].getSubject());
        assertEquals(createdUser.getEmail(), messages[0].getRecipients(Message.RecipientType.TO)[0].toString());

        mvc.perform(get(messages[0].getContent().toString().trim()))
                .andExpect(status().isOk());

        User user = userRepository.findByVerificationToken(createdUser.getVerificationToken()).orElseThrow(() -> UserNotFoundException.verificationTokenInvalid());

        assertEquals(User.UserStatus.ACTIVE, user.getStatus());

        GenericMessage<String> message = (GenericMessage<String>) testSupportBinder.messageCollector().forChannel(notificationChannel.approveUser()).take();

        String payload = message.getPayload();
        String expected = "{\"header\":{\"sender\":\"authorization-server\"},\"id\":" + user.getUuid() + "}";

        JSONAssert.assertEquals(expected, payload, JSONCompareMode.LENIENT);

        greenMail.stop();
    }
}
