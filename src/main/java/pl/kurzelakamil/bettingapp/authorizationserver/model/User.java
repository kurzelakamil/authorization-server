package pl.kurzelakamil.bettingapp.authorizationserver.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private Long uuid;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    private String verificationToken;

    public enum UserStatus {
        PENDING,
        ACTIVE,
        REJECTED;
    }

    public void generateVerificationToken(){
        verificationToken = UUID.randomUUID().toString();
    }

    public void approveUser(){
        status = UserStatus.ACTIVE;
    }
}
