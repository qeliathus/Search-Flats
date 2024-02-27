package by.potapchuk.userservice.service.api;

public interface IEmailService {

    void sendSimpleMessage(String emailTo, String subject, String text);
}
