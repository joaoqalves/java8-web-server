package net.joaoqalves.model.role;

public class InvalidRoleException extends Exception {
    public InvalidRoleException() {
    }

    public InvalidRoleException(String message) {
        super(message);
    }
}
