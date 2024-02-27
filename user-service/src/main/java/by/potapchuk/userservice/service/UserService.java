package by.potapchuk.userservice.service;

import by.potapchuk.userservice.aop.Audited;
import by.potapchuk.userservice.core.dto.UserCreationDto;
import by.potapchuk.userservice.core.dto.UserDetailsDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import by.potapchuk.userservice.core.dto.UserQueryDto;
import by.potapchuk.userservice.core.entity.User;
import by.potapchuk.userservice.core.entity.UserStatus;
import by.potapchuk.userservice.core.exceptions.EntityNotFoundException;
import by.potapchuk.userservice.core.exceptions.ValidationException;
import by.potapchuk.userservice.repository.UserRepository;
import by.potapchuk.userservice.service.api.IEmailMessageBuilder;
import by.potapchuk.userservice.service.api.IEmailService;
import by.potapchuk.userservice.service.api.ITemporarySecretTokenService;
import by.potapchuk.userservice.service.api.IUserPasswordEncoder;
import by.potapchuk.userservice.service.api.IUserService;
import by.potapchuk.userservice.tranformer.api.IUserTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static by.potapchuk.userservice.core.entity.AuditedAction.CREATE_USER;
import static by.potapchuk.userservice.core.entity.AuditedAction.INFO_ABOUT_ALL_USERS;
import static by.potapchuk.userservice.core.entity.AuditedAction.INFO_ABOUT_USER_BY_ID;
import static by.potapchuk.userservice.core.entity.AuditedAction.UPDATE_USER;
import static by.potapchuk.userservice.core.entity.EssenceType.USER;


@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final IUserTransformer IUserTransformer;
    private final IUserPasswordEncoder IUserPasswordEncoder;
    private final ITemporarySecretTokenService ITemporarySecretTokenService;
    private final IEmailService IEmailService;
    private final IEmailMessageBuilder IEmailMessageBuilder;

    public UserService(
            UserRepository userRepository,
            IUserTransformer IUserTransformer,
            IUserPasswordEncoder IUserPasswordEncoder,
            ITemporarySecretTokenService ITemporarySecretTokenService,
            IEmailService IEmailService,
            IEmailMessageBuilder IEmailMessageBuilder) {
        this.userRepository = userRepository;
        this.IUserTransformer = IUserTransformer;
        this.IUserPasswordEncoder = IUserPasswordEncoder;
        this.ITemporarySecretTokenService = ITemporarySecretTokenService;
        this.IEmailService = IEmailService;
        this.IEmailMessageBuilder = IEmailMessageBuilder;
    }

    @Override
    @Audited(auditedAction = INFO_ABOUT_ALL_USERS, essenceType = USER)
    public Page<UserInfoDto> getAllUsers(Pageable pageable) {
        Page<User> entityPage = userRepository.findAll(pageable);
        List<UserInfoDto> dtoList = entityPage.stream()
                .map(it -> IUserTransformer.transformInfoDtoFromEntity(it))
                .toList();
        return new PageImpl<UserInfoDto>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    @Override
    @Audited(auditedAction = INFO_ABOUT_USER_BY_ID, essenceType = USER)
    public UserInfoDto findUserById(UUID id) {
        User userEntity = getUserById(id);
        return IUserTransformer.transformInfoDtoFromEntity(userEntity);
    }

    @Transactional
    @Override
    @Audited(auditedAction = CREATE_USER, essenceType = USER)
    public User createUserByAdmin(UserCreationDto userCreationDto) {
        validateEmail(userCreationDto.getMail());
        User userForSave = userRepository.saveAndFlush(IUserTransformer.transformEntityFromCreateDto(userCreationDto));
        String token = ITemporarySecretTokenService.createToken(userForSave.getEmail());
//        emailService.sendSimpleMessage(
//                userCreationDto.getEmail(),
//                emailMessageBuilder.buildVerificationSubject(),
//                emailMessageBuilder.buildVerificationMessage(userCreationDto.getEmail(), token)
//        );
        return userForSave;
    }

    @Override
    @Audited(auditedAction = UPDATE_USER, essenceType = USER)
    public User updateUser(UserCreationDto userCreationDto, UUID id, LocalDateTime updatedDate) {
        User userEntity = getUserById(id);
        userEntity
                .setEmail(userCreationDto.getMail())
                .setPassword(IUserPasswordEncoder.encodePassword(userCreationDto.getPassword()))
                .setFio(userCreationDto.getFio())
                .setUserRole(userCreationDto.getRole())
                .setStatus(userCreationDto.getStatus());
        if (userEntity.getUpdateDate().truncatedTo(ChronoUnit.MILLIS).isEqual(updatedDate)) {
            userRepository.saveAndFlush(userEntity);
        } else {
            throw new ValidationException("version field - " + updatedDate
                    .toInstant(ZoneOffset.ofTotalSeconds(0))
                    .toEpochMilli());
        }
        return userEntity;
    }

    @Override
    public UserQueryDto getUserQueryDto(String email){
        return userRepository.findPasswordAndStatusByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User", email)
                );
    }

    @Override
    public UserDetailsDto getUserDetailsDto(String email) {
        return userRepository.findIdFioAndRoleByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User", email));
    }

    @Override
    public void activationUser(String email) {
        userRepository.updateStatusByEmail(UserStatus.ACTIVATED, email);
    }

    @Transactional
    @Override
    public User createUser(UserCreationDto userCreationDto) {
        validateEmail(userCreationDto.getMail());
        User userForSave = userRepository.saveAndFlush(IUserTransformer.transformEntityFromCreateDto(userCreationDto));
        String token = ITemporarySecretTokenService.createToken(userForSave.getEmail());
        IEmailService.sendSimpleMessage(
                userCreationDto.getMail(),
                IEmailMessageBuilder.buildVerificationSubject(),
                IEmailMessageBuilder.buildVerificationMessage(userCreationDto.getMail(), token)
        );
        return userForSave;
    }

    private User getUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    private void validateEmail(String email){
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("Данный mail уже зарегистрирован");
        }
    }
}
