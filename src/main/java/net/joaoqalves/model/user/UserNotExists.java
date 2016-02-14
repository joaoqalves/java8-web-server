package net.joaoqalves.model.user;

public class UserNotExists extends Exception {
    public UserNotExists() {
    }

    public UserNotExists(final String username) {
        super("User " + username + " does not exist");
    }
}
