package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import java.time.OffsetDateTime;

import lombok.Getter;

@Getter
public class RejectUserTransferObject {

    private HeaderDto header;
    private Long id;
    private RejectionReason rejectionReason;

    private enum RejectionReason{
        USER_ALREADY_EXISTS;
    }

    public RejectUserTransferObject(Long id){
        this.header = new HeaderDto("authorization-server", OffsetDateTime.now());
        this.id = id;
    }

    public void setUserAlreadyExistsReason(){
        this.rejectionReason = RejectionReason.USER_ALREADY_EXISTS;
    }
}
