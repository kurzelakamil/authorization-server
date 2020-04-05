package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckUserTransferObject {

    private HeaderDto header;
    private Long id;
    private String email;
    private String password;
}
