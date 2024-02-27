package by.potapchuk.userservice.service.api;

import by.potapchuk.userservice.core.dto.PasswordUpdateDto;
import by.potapchuk.userservice.core.dto.TemporarySecretTokenDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import by.potapchuk.userservice.core.dto.UserLoginDto;
import by.potapchuk.userservice.core.dto.UserRegistrationDto;
import by.potapchuk.userservice.core.entity.User;

public interface IAuthenticationService {

    User registrateUser(UserRegistrationDto userRegistrationDto);

    String loginUser(UserLoginDto userLoginDto);

    void verifyUserByEmailAndToken(TemporarySecretTokenDto temporarySecretTokenDto);

    UserInfoDto findInfoAboutMe();

    User updatePassword(PasswordUpdateDto passwordUpdateDto);

    void sendPasswordRestoreLink(String email);
}
