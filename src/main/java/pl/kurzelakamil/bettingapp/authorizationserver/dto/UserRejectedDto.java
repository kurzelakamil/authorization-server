package pl.kurzelakamil.bettingapp.authorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRejectedDto extends PayloadDto {

    public enum UserRejectionCode {
        USER_ALREADY_EXISTS(10);

        private int code;

        UserRejectionCode(int code){this.code = code;};
    }

    private Long id;

    @JsonValue
    private UserRejectionCode reason;
}
