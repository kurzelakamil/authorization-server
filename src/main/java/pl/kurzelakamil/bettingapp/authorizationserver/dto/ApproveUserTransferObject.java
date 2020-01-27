package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApproveUserTransferObject {

    private HeaderDto header;
    private Long id;

    public ApproveUserTransferObject(Long id){
        this.header = new HeaderDto("authorization-server", OffsetDateTime.now());
        this.id = id;
    }
}
