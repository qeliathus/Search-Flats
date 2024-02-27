package by.potapchuk.userservice.tranformer;

import by.potapchuk.userservice.core.dto.UserCreationDto;
import by.potapchuk.userservice.core.dto.UserDetailsDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import by.potapchuk.userservice.core.dto.UserRegistrationDto;
import by.potapchuk.userservice.core.entity.User;
import by.potapchuk.userservice.core.entity.UserRole;
import by.potapchuk.userservice.core.entity.UserStatus;
import by.potapchuk.userservice.service.api.IUserPasswordEncoder;
import by.potapchuk.userservice.tranformer.api.IUserTransformer;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class UserTransformer implements IUserTransformer {

    private final IUserPasswordEncoder IUserPasswordEncoder;

    public UserTransformer(IUserPasswordEncoder IUserPasswordEncoder) {
        this.IUserPasswordEncoder = IUserPasswordEncoder;
    }

    @Override
    public UserInfoDto transformInfoDtoFromEntity(User user) {
        return new UserInfoDto()
                .setId(user.getId())
                .setMail(user.getEmail())
                .setFio(user.getFio())
                .setRole(user.getUserRole())
                .setStatus(user.getStatus())
                .setCreatedDate(user.getCreationDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setUpdatedDate(user.getUpdateDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    @Override
    public UserCreationDto transformCreateDtoFromEntity(User user) {
        return new UserCreationDto()
                .setMail(user.getEmail())
                .setPassword(user.getPassword())
                .setFio(user.getFio())
                .setRole(user.getUserRole())
                .setStatus(user.getStatus());
    }

    @Override
    public User transformEntityFromCreateDto(UserCreationDto userCreationDto) {
        return new User()
                .setEmail(userCreationDto.getMail())
                .setPassword(IUserPasswordEncoder.encodePassword(userCreationDto.getPassword()))
                .setFio(userCreationDto.getFio())
                .setUserRole(userCreationDto.getRole())
                .setStatus(userCreationDto.getStatus());
    }

    @Override
    public User transformEntityFromRegistrationDto(UserRegistrationDto userRegistrationDto) {
        return new User()
                .setEmail(userRegistrationDto.getMail())
                .setPassword(IUserPasswordEncoder.encodePassword(userRegistrationDto.getPassword()))
                .setFio(userRegistrationDto.getFio())
                .setUserRole(UserRole.USER)
                .setStatus(UserStatus.WAITING_ACTIVATION);
    }

    @Override
    public UserCreationDto transformCreationDtoFromRegistrationDto(UserRegistrationDto userRegistrationDto){
        return new UserCreationDto().setMail(userRegistrationDto.getMail())
                .setPassword(userRegistrationDto.getPassword())
                .setFio(userRegistrationDto.getFio())
                .setStatus(UserStatus.WAITING_ACTIVATION)
                .setRole(UserRole.USER);
    }

    @Override
    public UserCreationDto transformCreationDtoFromDetailsDto(UserDetailsDto userDetailsDto, String password){
        return new UserCreationDto().setMail(userDetailsDto.getEmail())
                .setPassword(password)
                .setFio(userDetailsDto.getFio())
                .setRole(userDetailsDto.getRole())
                .setStatus(UserStatus.WAITING_ACTIVATION);
    }
}
