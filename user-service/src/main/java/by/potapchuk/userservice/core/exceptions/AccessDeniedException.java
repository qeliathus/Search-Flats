package by.potapchuk.userservice.core.exceptions;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("Данному токену авторизации запрещено выполнять запроса на данный адрес");
    }
}
