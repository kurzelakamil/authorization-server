package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HeaderDto {

    private final String sender = "authorization-server";
}
