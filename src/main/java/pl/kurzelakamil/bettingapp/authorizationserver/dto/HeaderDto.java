package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HeaderDto {

    private String sender;
    private LocalDateTime timeStamp;
}
