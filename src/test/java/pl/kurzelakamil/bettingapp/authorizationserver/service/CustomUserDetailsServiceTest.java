package pl.kurzelakamil.bettingapp.authorizationserver.service;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pl.kurzelakamil.bettingapp.authorizationserver.UnitTests;
import pl.kurzelakamil.bettingapp.authorizationserver.factory.UserFactory;
import pl.kurzelakamil.bettingapp.authorizationserver.model.User;
import pl.kurzelakamil.bettingapp.authorizationserver.model.UserPrincipal;
import pl.kurzelakamil.bettingapp.authorizationserver.repository.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@Category(UnitTests.class)
public class CustomUserDetailsServiceTest {

    UserRepository userRepository;
    CustomUserDetailsService customUserDetailsService;

    @Before
    public void setup(){
        userRepository = mock(UserRepository.class);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    public void loadUserByUsername(){
        User user = UserFactory.userNo1();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername("testEmail");
        assertEquals(userPrincipal.getId(), user.getId());
        assertEquals(userPrincipal.getPassword(), user.getPassword());
        assertEquals(userPrincipal.getUsername(), user.getEmail());
        assertEquals(userPrincipal.getRole(), user.getRole());
        assertEquals(userPrincipal.getAuthorities().size(), 1);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_usernameNotFound(){
        when(userRepository.findByEmailOrUuid(anyString(), anyLong())).thenReturn(Optional.empty());

        customUserDetailsService.loadUserByUsername("testEmail");
    }
}
