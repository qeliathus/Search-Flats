package by.potapchuk.userservice.core.exceptions;

public class InvalidLinkException extends RuntimeException {

    public InvalidLinkException() {
        super("Invalid link");
    }
}
