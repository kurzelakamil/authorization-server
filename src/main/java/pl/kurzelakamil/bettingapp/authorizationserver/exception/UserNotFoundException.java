package pl.kurzelakamil.bettingapp.authorizationserver.exception;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -7800603362317551073L;

    public UserNotFoundException(String msg){
        super(msg);
    }

    public static UserNotFoundException verificationTokenInvalid(){
        return new UserNotFoundException("Verification token is invalid");
    }

    public static UserNotFoundException emailNotFound(String email){
        return new UserNotFoundException(String.format("User with email: %s not found", email));
    }

}
