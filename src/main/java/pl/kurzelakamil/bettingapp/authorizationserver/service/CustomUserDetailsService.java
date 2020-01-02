package pl.kurzelakamil.bettingapp.authorizationserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pl.kurzelakamil.bettingapp.authorizationserver.model.User;
import pl.kurzelakamil.bettingapp.authorizationserver.model.UserPrincipal;
import pl.kurzelakamil.bettingapp.authorizationserver.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
        List<GrantedAuthority> authorities = getGrantedAuthorities(user);
        return new UserPrincipal(user, authorities);
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName().name()));
        return authorities;
    }
}
