package pl.kurzelakamil.bettingapp.authorizationserver.service;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import pl.kurzelakamil.bettingapp.authorizationserver.UnitTests;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.SagaTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserApprovedDto;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserInputDto;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserRejectedDto;
import pl.kurzelakamil.bettingapp.authorizationserver.factory.UserFactory;
import pl.kurzelakamil.bettingapp.authorizationserver.factory.UserInputDtoFactory;
import pl.kurzelakamil.bettingapp.authorizationserver.api.NotificationClient;
import pl.kurzelakamil.bettingapp.authorizationserver.model.Role;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;
import pl.kurzelakamil.bettingapp.authorizationserver.repository.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Category(UnitTests.class)
public class UserServiceTest {

    NotificationClient notificationClient;
    UserRepository userRepository;
    UserService userService;

    @Before
    public void setup(){
        userRepository = mock(UserRepository.class);
        notificationClient = mock(NotificationClient.class);
        userService = new UserService(notificationClient, userRepository);
    }

    @Test
    public void validateCreatedUser_reject(){
        UserInputDto userInputDto = UserInputDtoFactory.userInputDtoNo1();
        User user = UserFactory.userNo1();
        when(userRepository.findByEmail(userInputDto.getEmail())).thenReturn(Optional.of(user));

        userService.validateCreatedUser(userInputDto);

        ArgumentCaptor<SagaTransferObject> sagaTransferObjectArgumentCaptor = ArgumentCaptor.forClass(SagaTransferObject.class);
        verify(notificationClient, times(1)).rejectUser(sagaTransferObjectArgumentCaptor.capture());

        SagaTransferObject sagaTransferObject = sagaTransferObjectArgumentCaptor.getValue();
        UserRejectedDto payload = (UserRejectedDto) sagaTransferObject.getPayload();

        assertEquals(sagaTransferObject.getHeader().getSender(), "authorization-server");
        assertEquals(payload.getId(), userInputDto.getId());
        assertEquals(payload.getReason(), UserRejectedDto.UserRejectionCode.USER_ALREADY_EXISTS);
    }

    @Test
    public void validateCreatedUser_approve(){
        UserInputDto userInputDto = UserInputDtoFactory.userInputDtoNo1();
        when(userRepository.findByEmail(userInputDto.getEmail())).thenReturn(Optional.empty());

        userService.validateCreatedUser(userInputDto);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User user = userArgumentCaptor.getValue();

        assertEquals(user.getEmail(), userInputDto.getEmail());
        assertEquals(user.getPassword(), userInputDto.getPassword());
        assertEquals(user.getUuid(), userInputDto.getId());
        assertEquals(user.getRole(), Role.user());

        ArgumentCaptor<SagaTransferObject> sagaTransferObjectArgumentCaptor = ArgumentCaptor.forClass(SagaTransferObject.class);
        verify(notificationClient, times(1)).approveUser(sagaTransferObjectArgumentCaptor.capture());

        SagaTransferObject sagaTransferObject = sagaTransferObjectArgumentCaptor.getValue();
        UserApprovedDto payload = (UserApprovedDto) sagaTransferObject.getPayload();

        assertEquals(sagaTransferObject.getHeader().getSender(), "authorization-server");
        assertEquals(payload.getId(), userInputDto.getId());
    }
}
