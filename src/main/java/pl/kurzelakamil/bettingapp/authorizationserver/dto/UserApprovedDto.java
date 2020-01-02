package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserApprovedDto extends PayloadDto {

    private Long id;
}
