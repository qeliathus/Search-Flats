package by.potapchuk.userservice.service;

import by.potapchuk.userservice.service.api.IEmailMessageBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageBuilder implements IEmailMessageBuilder {

    @Value("${custom.email.messages.verification}")
    private String verificationMessage;
    @Value("${custom.email.messages.updatePassword}")
    private String updatePasswordMessage;

    private static final String VERIFICATION_SUBJECT = "Verification user";
    private static final String UPDATE_PASSWORD_SUBJECT = "Update user password";

    @Override
    public String buildVerificationSubject() {
        return VERIFICATION_SUBJECT;
    }

    public String buildUpdatePasswordSubject() {
        return UPDATE_PASSWORD_SUBJECT;
    }

    @Override
    public String buildVerificationMessage(String email, String token) {
        return verificationMessage.formatted(email, token);
    }

    @Override
    public String buildUpdatePasswordMessage(String token) {
        return updatePasswordMessage.formatted(token);
    }
}