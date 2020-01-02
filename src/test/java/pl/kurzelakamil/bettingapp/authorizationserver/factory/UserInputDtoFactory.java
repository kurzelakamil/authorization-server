package pl.kurzelakamil.bettingapp.authorizationserver.factory;

import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserInputDto;

public class UserInputDtoFactory {

    public static UserInputDto userInputDtoNo1(){
        UserInputDto userInputDto = new UserInputDto();
        userInputDto.setId(1L);
        userInputDto.setEmail("testEmail");
        userInputDto.setPassword("testPassword");
        return userInputDto;
    }
}
