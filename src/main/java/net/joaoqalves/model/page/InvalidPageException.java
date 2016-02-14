package net.joaoqalves.model.page;

public class InvalidPageException extends Exception {

    public InvalidPageException() {
    }

    public InvalidPageException(String page) {
        super("Page " + page + " does not match with any route in the DB");
    }
}
