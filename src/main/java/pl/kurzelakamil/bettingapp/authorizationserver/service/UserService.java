package pl.kurzelakamil.bettingapp.authorizationserver.service;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import pl.kurzelakamil.bettingapp.authorizationserver.api.NotificationService;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.CheckUserTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.mapper.UserMapper;
import pl.kurzelakamil.bettingapp.authorizationserver.model.Role;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;
import pl.kurzelakamil.bettingapp.authorizationserver.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private static final UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    private UserRepository userRepository;
    private NotificationService notificationService;

    public void validateCreatedUser(CheckUserTransferObject checkUserTransferObject){
        Optional<User> optionalUser = userRepository.findByEmail(checkUserTransferObject.getEmail());
        if(optionalUser.isPresent()){
            rejectCreatedUser(checkUserTransferObject.getId());
        } else {
            approveCreatedUser(checkUserTransferObject);
        }
    }

    private void approveCreatedUser(CheckUserTransferObject checkUserTransferObject){
        User user = MAPPER.checkUserDtoToUser(checkUserTransferObject);
        user.setRole(Role.user());
        user.setUuid(checkUserTransferObject.getId());
        userRepository.save(user);
        notificationService.approveUser(user);
    }

    private void rejectCreatedUser(Long id){
        notificationService.rejectUser(id);
    }

}
