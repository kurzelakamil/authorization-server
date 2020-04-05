package pl.kurzelakamil.bettingapp.authorizationserver.notification;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import pl.kurzelakamil.bettingapp.authorizationserver.dto.ApproveUserTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.dto.RejectUserTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;

import lombok.AllArgsConstructor;

@Service
@EnableBinding(NotificationChannel.class)
@AllArgsConstructor
public class NotificationService {

    private NotificationChannel notificationChannel;

    @SendTo(NotificationChannel.APPROVE_USER)
    public void approveUser(User user){
        ApproveUserTransferObject transferObject = new ApproveUserTransferObject(user.getUuid());
        Message<ApproveUserTransferObject> message = MessageBuilder.withPayload(transferObject).build();
        notificationChannel.approveUser().send(message);
    }

    @SendTo(NotificationChannel.REJECT_USER)
    public void rejectUser(Long id){
        RejectUserTransferObject transferObject = new RejectUserTransferObject(id);
        transferObject.setUserAlreadyExistsReason();
        Message<RejectUserTransferObject> message = MessageBuilder.withPayload(transferObject).build();
        notificationChannel.rejectUser().send(message);
    }
}
