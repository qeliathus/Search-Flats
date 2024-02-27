package by.potapchuk.userservice.controller;

import by.potapchuk.userservice.core.dto.MessageResponse;
import by.potapchuk.userservice.core.dto.PageDto;
import by.potapchuk.userservice.core.dto.PasswordUpdateDto;
import by.potapchuk.userservice.core.dto.TemporarySecretTokenDto;
import by.potapchuk.userservice.core.dto.UserCreationDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import by.potapchuk.userservice.core.dto.UserLoginDto;
import by.potapchuk.userservice.core.dto.UserRegistrationDto;
import by.potapchuk.userservice.service.api.IAuthenticationService;
import by.potapchuk.userservice.service.api.IUserService;
import by.potapchuk.userservice.tranformer.api.IPageTransformer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final IUserService IUserService;
    private final IAuthenticationService IAuthenticationService;
    private final IPageTransformer IPageTransformer;

    public UserController(
            IUserService IUserService,
            IAuthenticationService IAuthenticationService,
            IPageTransformer IPageTransformer
    ) {
        this.IUserService = IUserService;
        this.IAuthenticationService = IAuthenticationService;
        this.IPageTransformer = IPageTransformer;
    }

    @GetMapping
    public PageDto<UserInfoDto> getAllUsers(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return IPageTransformer.transformPageDtoFromPage(IUserService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public UserInfoDto getUserById(
            @PathVariable UUID id
    ) {
        return IUserService.findUserById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createUser(@Validated @RequestBody UserCreationDto userCreationDto) {
        IUserService.createUserByAdmin(userCreationDto);
    }

    @PutMapping("/{id}/dt_update/{dt_update}")
    public void updateUser(
            @PathVariable(name = "id") UUID id,
            @PathVariable(name="dt_update") Long updateDate,
            @Validated @RequestBody UserCreationDto userCreationDto) {
        LocalDateTime updatedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(updateDate), ZoneId.systemDefault());
        IUserService.updateUser(userCreationDto, id, updatedDate);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public void registerUser(@Validated @RequestBody UserRegistrationDto userRegistrationDto) {
        IAuthenticationService.registrateUser(userRegistrationDto);
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@Validated @RequestBody UserLoginDto userLoginDto) {
        String token = IAuthenticationService.loginUser(userLoginDto);
        return ResponseEntity.ok().header("Authorization", token).body(token);
    }

    @GetMapping("/verification")
    public void verifyUser(
            @Validated @Email @NotNull @RequestParam String email,
            @Validated @Size(min = 36, max = 36, message = "size must be 36") @NotNull @RequestParam String token
    ) {
        TemporarySecretTokenDto temporarySecretTokenDto = new TemporarySecretTokenDto(email, UUID.fromString(token));
        IAuthenticationService.verifyUserByEmailAndToken(temporarySecretTokenDto);
    }

    @GetMapping("/me")
    public UserInfoDto getInfoAboutMe() {
        return IAuthenticationService.findInfoAboutMe();
    }

    @PostMapping("/send-password-restore-link")
    public MessageResponse sendPasswordRestoreLink(
            @Validated
            @Email
            @NotNull
            @RequestParam String email
    ) {
        IAuthenticationService.sendPasswordRestoreLink(email);
        return new MessageResponse("Ссылка на сброс пароля отправлена вам на почту");
    }

    @PostMapping("/update-password")
    public MessageResponse updatePassword(@Validated @RequestBody PasswordUpdateDto passwordUpdateDto) {
        IAuthenticationService.updatePassword(passwordUpdateDto);
        return new MessageResponse("Пароль изменен");
    }
}