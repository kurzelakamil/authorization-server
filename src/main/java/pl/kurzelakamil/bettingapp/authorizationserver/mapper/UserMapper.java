package pl.kurzelakamil.bettingapp.authorizationserver.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.kurzelakamil.bettingapp.authorizationserver.dto.UserInputDto;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;

@Mapper
public interface UserMapper {

    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "role", ignore = true)
    User userInputDtoToUser(UserInputDto userInputDto);
}
