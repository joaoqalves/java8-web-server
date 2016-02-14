package net.joaoqalves.model.user;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String username) {
        super("User " + username + " already exists");
    }
}
