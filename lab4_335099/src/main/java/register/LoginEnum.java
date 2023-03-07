package register;

public enum LoginEnum {

    TOKEN_TROUBLES("Invalid token"),
    SUCCESS("Success auth"),
    IS_AUTH("User already auth"),
    USERNAME_EXISTS("User with this username already exists"),
    WRONG_USER_OR_PASSWORD("There are no user with such username and password"),
    WRONG_PASSWORD("Wrong password"),
    WRONG_USERNAME("User with this username does not exist"),
    ERROR("Something went wring");

    private final String message;

    LoginEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
