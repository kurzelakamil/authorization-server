package pl.kurzelakamil.bettingapp.authorizationserver.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.kurzelakamil.bettingapp.authorizationserver.dto.CheckUserTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verificationToken", ignore = true)
    User checkUserDtoToUser(CheckUserTransferObject checkUserTransferObject);
}
