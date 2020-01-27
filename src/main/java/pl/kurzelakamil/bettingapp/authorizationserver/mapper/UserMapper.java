package pl.kurzelakamil.bettingapp.authorizationserver.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.kurzelakamil.bettingapp.authorizationserver.dto.CheckUserTransferObject;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;

@Mapper
public interface UserMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "role", ignore = true)
    User checkUserDtoToUser(CheckUserTransferObject checkUserTransferObject);
}
