package by.potapchuk.userservice.service;

import by.potapchuk.userservice.core.entity.TemporarySecretToken;
import by.potapchuk.userservice.core.exceptions.EntityNotFoundException;
import by.potapchuk.userservice.core.exceptions.InvalidLinkException;
import by.potapchuk.userservice.repository.TemporarySecretTokenRepository;
import by.potapchuk.userservice.service.api.ITemporarySecretTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TemporarySecretTokenService implements ITemporarySecretTokenService {

    private final TemporarySecretTokenRepository temporarySecretTokenRepository;

    public TemporarySecretTokenService(TemporarySecretTokenRepository temporarySecretTokenRepository) {
        this.temporarySecretTokenRepository = temporarySecretTokenRepository;
    }

    @Transactional
    @Override
    public String createToken(String email) {
        UUID token = UUID.randomUUID();
        TemporarySecretToken entity = new TemporarySecretToken(email, token);
        TemporarySecretToken result = temporarySecretTokenRepository.save(entity);
        return result.getSecretToken().toString();
    }

    @Override
    public String getEmailByToken(String token) {
        String email = temporarySecretTokenRepository.findEmailByToken(UUID.fromString(token));
        if (email == null) {
            throw new InvalidLinkException();
        }
        return email;
    }


    @Transactional
    @Override
    public void deleteEntityByEmailAndToken(String email, String token) {
        if (temporarySecretTokenRepository.deleteEntityByEmailAndToken(email, UUID.fromString(token)) == 0) {
            throw new EntityNotFoundException("User", email);
        }
        ;
    }
}
