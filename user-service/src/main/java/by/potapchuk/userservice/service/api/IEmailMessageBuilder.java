package by.potapchuk.userservice.service.api;

public interface IEmailMessageBuilder {

    String buildVerificationMessage(String email, String token);

    String buildUpdatePasswordMessage(String token);

    String buildVerificationSubject();

    String buildUpdatePasswordSubject();
}
