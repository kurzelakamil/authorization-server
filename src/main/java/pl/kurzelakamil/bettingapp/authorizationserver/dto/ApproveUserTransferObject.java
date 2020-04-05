package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ApproveUserTransferObject {

    private HeaderDto header;
    private Long id;

    public ApproveUserTransferObject(Long id){
        this.header = new HeaderDto("authorization-server", LocalDateTime.now());
        this.id = id;
    }
}
