package net.joaoqalves.model.user;

public class UserNotFoundException extends Exception {

    public static final String DEFAULT_MSG = "User not found";

    public UserNotFoundException() {
        super(DEFAULT_MSG);
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }

}
