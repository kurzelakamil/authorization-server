package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInputDto extends PayloadDto {

    private Long id;
    private String email;
    private String password;
}
