package pl.kurzelakamil.bettingapp.authorizationserver.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import pl.kurzelakamil.bettingapp.authorizationserver.api.NotificationService;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.CheckUserTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.mapper.UserMapper;
import pl.kurzelakamil.bettingapp.authorizationserver.model.Role;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;
import pl.kurzelakamil.bettingapp.authorizationserver.repository.UserRepository;

@Service
public class UserService {

    private static final UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    private JavaMailSender mailSender;
    private UserRepository userRepository;
    private NotificationService notificationService;

    @Value("${activation.url}")
    private String activationUrl;

    public UserService(JavaMailSender mailSender, UserRepository userRepository, NotificationService notificationService){
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.notificationService = notificationService;


    public void validateUser(CheckUserTransferObject checkUserTransferObject){
        userRepository.findByEmail(checkUserTransferObject.getEmail())
                .ifPresentOrElse(user -> rejectUser(checkUserTransferObject.getId()), () -> prepareUserInPendingStatus(checkUserTransferObject));
    }

    public void activateUser(String token){
        userRepository.findByVerificationToken(token).ifPresentOrElse(user -> approveUser(user), () -> new RuntimeException());
    }

    private void prepareUserInPendingStatus(CheckUserTransferObject checkUserTransferObject){
        User user = MAPPER.checkUserDtoToUser(checkUserTransferObject);
        user.setRole(Role.user());
        user.setUuid(checkUserTransferObject.getId());
        user.generateVerificationToken();
        sendActivationEmail(user);
    }

    private void approveUser(User user){
        user.approveUser();
        userRepository.save(user);
        notificationService.approveUser(user);
    }

    private void rejectUser(Long id){
        notificationService.rejectUser(id);
    }

    private void sendActivationEmail(User user){
        String url = activationUrl + "?token=" + user.getVerificationToken();
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Account activation");
        email.setText(url);
        mailSender.send(email);
    }

}
