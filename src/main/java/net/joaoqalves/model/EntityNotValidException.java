package net.joaoqalves.model;

public class EntityNotValidException extends Exception {
    public EntityNotValidException() {
    }

    public EntityNotValidException(String message) {
        super(message);
    }
}
