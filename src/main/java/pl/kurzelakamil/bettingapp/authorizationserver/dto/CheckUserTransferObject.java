package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import lombok.Getter;

@Getter
public class CheckUserTransferObject {

    private HeaderDto headerDto;
    private Long id;
    private String email;
    private String password;
}
