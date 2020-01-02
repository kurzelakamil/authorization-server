package pl.kurzelakamil.bettingapp.authorizationserver.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UserPrincipal extends org.springframework.security.core.userdetails.User {

    public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();

    }

    private Long id;
    private String username;
    private String password;
    private Role role;
}

