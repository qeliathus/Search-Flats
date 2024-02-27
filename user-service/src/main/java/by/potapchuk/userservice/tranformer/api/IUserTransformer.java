package by.potapchuk.userservice.tranformer.api;


import by.potapchuk.userservice.core.dto.UserCreationDto;
import by.potapchuk.userservice.core.dto.UserDetailsDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import by.potapchuk.userservice.core.dto.UserRegistrationDto;
import by.potapchuk.userservice.core.entity.User;

public interface IUserTransformer {

    UserInfoDto transformInfoDtoFromEntity(User user);

    UserCreationDto transformCreateDtoFromEntity(User user);

    User transformEntityFromCreateDto(UserCreationDto userCreationDto);

    User transformEntityFromRegistrationDto(UserRegistrationDto userRegistrationDto);

    UserCreationDto transformCreationDtoFromRegistrationDto(UserRegistrationDto userRegistrationDto);

    UserCreationDto transformCreationDtoFromDetailsDto(UserDetailsDto userDetailsDto, String password);
}
