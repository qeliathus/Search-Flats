package by.potapchuk.userservice.service.api;

public interface ITemporarySecretTokenService {

    String createToken(String email);

    String getEmailByToken(String token);

    void deleteEntityByEmailAndToken(String email, String token);

}
