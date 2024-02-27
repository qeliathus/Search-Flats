package by.potapchuk.userservice.service;

import by.potapchuk.userservice.aop.Audited;
import by.potapchuk.userservice.core.dto.PasswordUpdateDto;
import by.potapchuk.userservice.core.dto.TemporarySecretTokenDto;
import by.potapchuk.userservice.core.dto.UserDetailsDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import by.potapchuk.userservice.core.dto.UserLoginDto;
import by.potapchuk.userservice.core.dto.UserQueryDto;
import by.potapchuk.userservice.core.dto.UserRegistrationDto;
import by.potapchuk.userservice.core.entity.User;
import by.potapchuk.userservice.core.entity.UserStatus;
import by.potapchuk.userservice.service.api.IAuthenticationService;
import by.potapchuk.userservice.service.api.IEmailMessageBuilder;
import by.potapchuk.userservice.service.api.IEmailService;
import by.potapchuk.userservice.service.api.ITemporarySecretTokenService;
import by.potapchuk.userservice.service.api.IUserPasswordEncoder;
import by.potapchuk.userservice.service.api.IUserService;
import by.potapchuk.userservice.service.jwt.JwtHandler;
import by.potapchuk.userservice.tranformer.api.IUserTransformer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static by.potapchuk.userservice.core.entity.AuditedAction.INFO_ABOUT_ME;
import static by.potapchuk.userservice.core.entity.AuditedAction.LOGIN;
import static by.potapchuk.userservice.core.entity.AuditedAction.REGISTRATION;
import static by.potapchuk.userservice.core.entity.AuditedAction.UPDATE_PASSWORD;
import static by.potapchuk.userservice.core.entity.AuditedAction.VERIFICATION;
import static by.potapchuk.userservice.core.entity.EssenceType.USER;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final IUserService IUserService;
    private final IUserTransformer IUserTransformer;
    private final IUserPasswordEncoder IUserPasswordEncoder;
    private final IEmailService IEmailService;
    private final IEmailMessageBuilder IEmailMessageBuilder;
    private final ITemporarySecretTokenService ITemporarySecretTokenService;
    private final JwtHandler jwtHandler;

    public AuthenticationService(IUserService IUserService,
                                 IUserTransformer IUserTransformer,
                                 IUserPasswordEncoder IUserPasswordEncoder,
                                 IEmailService IEmailService,
                                 IEmailMessageBuilder IEmailMessageBuilder,
                                 ITemporarySecretTokenService ITemporarySecretTokenService,
                                 JwtHandler jwtHandler) {
        this.IUserService = IUserService;
        this.IUserTransformer = IUserTransformer;
        this.IUserPasswordEncoder = IUserPasswordEncoder;
        this.IEmailService = IEmailService;
        this.IEmailMessageBuilder = IEmailMessageBuilder;
        this.ITemporarySecretTokenService = ITemporarySecretTokenService;
        this.jwtHandler = jwtHandler;
    }

    @Transactional
    @Override
    @Audited(auditedAction = REGISTRATION, essenceType = USER)
    public User registrateUser(UserRegistrationDto userRegistrationDto) {
        return IUserService.createUser(
                IUserTransformer.transformCreationDtoFromRegistrationDto(userRegistrationDto)
        );
    }

    @Override
    @Audited(auditedAction = LOGIN, essenceType = USER)
    public String loginUser(UserLoginDto userLoginDto) {
        UserQueryDto userQueryDto = IUserService.getUserQueryDto(userLoginDto.getMail());

        if (!IUserPasswordEncoder.passwordMatches(userLoginDto.getPassword(), userQueryDto.getPassword())) {
            throw new RuntimeException("wrong password");
        }
        if (!userQueryDto.getStatus().equals(UserStatus.ACTIVATED)) {
            throw new RuntimeException("verification failed or your account deactivated");
        }

        return jwtHandler.generateAccessToken(IUserService.getUserDetailsDto(userLoginDto.getMail()));
    }

    @Transactional
    @Override
    @Audited(auditedAction = VERIFICATION, essenceType = USER)
    public void verifyUserByEmailAndToken(TemporarySecretTokenDto temporarySecretTokenDto) {
        String email = ITemporarySecretTokenService.getEmailByToken(temporarySecretTokenDto.getSecretToken().toString());
        IUserService.activationUser(email);
        ITemporarySecretTokenService.deleteEntityByEmailAndToken(
                email,
                temporarySecretTokenDto.getSecretToken().toString()
        );
    }

    @Override
    @Audited(auditedAction = INFO_ABOUT_ME, essenceType = USER)
    public UserInfoDto findInfoAboutMe() {
        UserDetailsDto userDetailsDto = (UserDetailsDto) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return IUserService.findUserById(userDetailsDto.getId());
    }

    @Transactional
    @Override
    public void sendPasswordRestoreLink(String email) {
        String token = ITemporarySecretTokenService.createToken(email);
        IEmailService.sendSimpleMessage(
                email,
                IEmailMessageBuilder.buildUpdatePasswordSubject(),
                IEmailMessageBuilder.buildUpdatePasswordMessage(token));
    }

    @Transactional
    @Override
    @Audited(auditedAction = UPDATE_PASSWORD, essenceType = USER)
    public User updatePassword(PasswordUpdateDto passwordUpdateDto) {
        String email = ITemporarySecretTokenService.getEmailByToken(passwordUpdateDto.getToken().toString());
        UserDetailsDto userDetailsDto = IUserService.getUserDetailsDto(email);
        ITemporarySecretTokenService.deleteEntityByEmailAndToken(email, passwordUpdateDto.getToken().toString());
        IUserService.createUserByAdmin(IUserTransformer.transformCreationDtoFromDetailsDto(userDetailsDto, passwordUpdateDto.getPassword()));
        return IUserService.createUserByAdmin(IUserTransformer
                .transformCreationDtoFromDetailsDto(userDetailsDto, passwordUpdateDto.getPassword()));
    }
}