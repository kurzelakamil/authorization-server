package pl.kurzelakamil.bettingapp.authorizationserver.service;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import pl.kurzelakamil.bettingapp.authorizationserver.dto.HeaderDto;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.SagaTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserApprovedDto;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserInputDto;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserRejectedDto;
import pl.kurzelakamil.bettingapp.authorizationserver.api.NotificationClient;
import pl.kurzelakamil.bettingapp.authorizationserver.mapper.UserMapper;
import pl.kurzelakamil.bettingapp.authorizationserver.model.Role;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;
import pl.kurzelakamil.bettingapp.authorizationserver.repository.UserRepository;

import lombok.AllArgsConstructor;

import static pl.kurzelakamil.bettingapp.authorizationserver.dto.UserRejectedDto.UserRejectionCode.*;

@Service
@AllArgsConstructor
public class UserService {

    private static final UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    private NotificationClient notificationClient;
    private UserRepository userRepository;

    protected void validateCreatedUser(UserInputDto userInputDto){
        Optional<User> optionalUser = userRepository.findByEmail(userInputDto.getEmail());
        if(optionalUser.isPresent()){
            rejectCreatedUser(userInputDto);
        } else {
            approveCreatedUser(userInputDto);
        }
    }

    private void approveCreatedUser(UserInputDto userInputDto){
        User user = MAPPER.userInputDtoToUser(userInputDto);
        user.setRole(Role.user());
        userRepository.save(user);
        SagaTransferObject sagaTransferObject = prepareSagaTransferObject();
        sagaTransferObject.setPayload(new UserApprovedDto(user.getUuid()));
        notificationClient.approveUser(sagaTransferObject);
    }

    private void rejectCreatedUser(UserInputDto userInputDto){
        SagaTransferObject sagaTransferObject = prepareSagaTransferObject();
        sagaTransferObject.setPayload(new UserRejectedDto(userInputDto.getId(), USER_ALREADY_EXISTS));
        notificationClient.rejectUser(sagaTransferObject);
    }

    private SagaTransferObject prepareSagaTransferObject(){
        SagaTransferObject sagaTransferObject = new SagaTransferObject();
        sagaTransferObject.setHeader(new HeaderDto());
        return sagaTransferObject;
    }
}
