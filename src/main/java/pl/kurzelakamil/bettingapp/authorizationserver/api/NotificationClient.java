package pl.kurzelakamil.bettingapp.authorizationserver.api;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import pl.kurzelakamil.bettingapp.authorizationserver.dto.SagaTransferObject;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class NotificationClient {

    private SagaClient sagaClient;

    @SendTo(SagaClient.SAGA_USER_APPROVED)
    public void approveUser(SagaTransferObject sagaTransferObject){
        Message<SagaTransferObject> message = MessageBuilder.withPayload(sagaTransferObject).build();
        sagaClient.sagaUserApproved().send(message);
    }

    @SendTo(SagaClient.SAGA_USER_REJECTED)
    public void rejectUser(SagaTransferObject sagaTransferObject){
        Message<SagaTransferObject> message = MessageBuilder.withPayload(sagaTransferObject).build();
        sagaClient.sagaUserRejected().send(message);
    }
}
