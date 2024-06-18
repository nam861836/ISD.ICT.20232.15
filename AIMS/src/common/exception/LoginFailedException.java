package common.exception;

;


public class LoginFailedException extends AimsException {

    public LoginFailedException() {

    }

    public LoginFailedException(String message) {
        super(message);
    }

}