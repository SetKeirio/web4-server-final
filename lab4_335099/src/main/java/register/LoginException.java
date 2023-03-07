package register;

public class LoginException extends RuntimeException{
    private final LoginEnum data;


    public LoginException(LoginEnum data) {
        this.data = data;
    }

    public LoginEnum getData() {
        return data;
    }
}
