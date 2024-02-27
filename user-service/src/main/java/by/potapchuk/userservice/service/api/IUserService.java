package by.potapchuk.userservice.service.api;

import by.potapchuk.userservice.core.dto.UserCreationDto;
import by.potapchuk.userservice.core.dto.UserDetailsDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import by.potapchuk.userservice.core.dto.UserQueryDto;
import by.potapchuk.userservice.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.UUID;

public interface IUserService {

    Page<UserInfoDto> getAllUsers(Pageable pageable);

    UserInfoDto findUserById(UUID id);

    User createUserByAdmin(UserCreationDto userCreationDto);

    User createUser(UserCreationDto userCreationDto);

    User updateUser(UserCreationDto userCreationDto, UUID id, LocalDateTime updateDate);

    UserQueryDto getUserQueryDto(String email);

    UserDetailsDto getUserDetailsDto(String email);

    void activationUser(String email);
}
