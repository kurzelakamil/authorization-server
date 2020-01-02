package pl.kurzelakamil.bettingapp.authorizationserver.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface SagaClient {

    String USER_CREATED = "userCreated";
    String SAGA_USER_APPROVED = "sagaUserApproved";
    String SAGA_USER_REJECTED = "sagaUserRejected";

    @Input
    SubscribableChannel userCreated();

    @Output
    MessageChannel sagaUserApproved();

    @Output
    MessageChannel sagaUserRejected();
}
