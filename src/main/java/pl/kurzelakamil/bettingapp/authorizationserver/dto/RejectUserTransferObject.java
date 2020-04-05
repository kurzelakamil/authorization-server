package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RejectUserTransferObject {

    private HeaderDto header;
    private Long id;
    private RejectionReason rejectionReason;

    private enum RejectionReason{
        USER_ALREADY_EXISTS
    }

    public RejectUserTransferObject(Long id){
        this.header = new HeaderDto("authorization-server", LocalDateTime.now());
        this.id = id;
    }

    public void setUserAlreadyExistsReason(){
        this.rejectionReason = RejectionReason.USER_ALREADY_EXISTS;
    }
}
