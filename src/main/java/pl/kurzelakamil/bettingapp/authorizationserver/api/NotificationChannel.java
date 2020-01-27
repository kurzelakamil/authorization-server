package pl.kurzelakamil.bettingapp.authorizationserver.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface NotificationChannel {

    String CHECK_USER = "checkUser";
    String APPROVE_USER = "approveUser";
    String REJECT_USER = "rejectUser";

    @Input
    SubscribableChannel checkUser();

    @Output
    MessageChannel approveUser();

    @Output
    MessageChannel rejectUser();
}
