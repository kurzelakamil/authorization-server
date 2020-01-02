package pl.kurzelakamil.bettingapp.authorizationserver.factory;

import pl.kurzelakamil.bettingapp.authorizationserver.model.Role;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;

public class UserFactory {

    public static User userNo1() {
        User user = new User();
        user.setId(1L);
        user.setEmail("testEmail");
        user.setPassword("testPassword");
        user.setUuid(1L);
        user.setRole(Role.user());
        return user;
    }
}
