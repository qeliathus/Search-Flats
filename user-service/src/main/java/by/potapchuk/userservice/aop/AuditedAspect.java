package by.potapchuk.userservice.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import by.potapchuk.userservice.core.dto.Emailable;
import by.potapchuk.userservice.core.dto.UserDetailsDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import by.potapchuk.userservice.core.dto.Userable;
import by.potapchuk.userservice.core.dto.audit.AuditDto;
import by.potapchuk.userservice.core.dto.audit.UserAuditDto;
import by.potapchuk.userservice.core.entity.User;
import by.potapchuk.userservice.core.entity.UserRole;
import by.potapchuk.userservice.core.exceptions.EntityNotFoundException;
import by.potapchuk.userservice.httpclient.AuditFeignClient;
import by.potapchuk.userservice.repository.UserRepository;
import by.potapchuk.userservice.service.jwt.JwtHandler;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
public class AuditedAspect {

    private final UserRepository userRepository;
    private final AuditFeignClient auditFeignClient;
    private final JwtHandler jwtHandler;

    public AuditedAspect(UserRepository userRepository, AuditFeignClient auditFeignClient, JwtHandler jwtHandler) {
        this.userRepository = userRepository;
        this.auditFeignClient = auditFeignClient;
        this.jwtHandler = jwtHandler;
    }

    @Around("@annotation(Audited)")
    public Object checkActivation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Audited annotation = signature.getMethod().getAnnotation(Audited.class);
        Object result = joinPoint.proceed();
        AuditDto auditDto = buildAuditDto(joinPoint, annotation, result);
        String token = "Bearer " + jwtHandler.generateAccessToken(new UserDetailsDto().setRole(UserRole.SYSTEM));
        auditFeignClient.sendRequestToCreateLog(token, auditDto);
        return result;
    }

    private AuditDto buildAuditDto(ProceedingJoinPoint joinPoint, Audited annotation, Object result) {
        switch (annotation.auditedAction()) {
            case REGISTRATION, UPDATE_PASSWORD -> {
                return createAuditDto(annotation, (User) result);
            }
            case VERIFICATION, LOGIN -> {
                return getAuditDtoByEmail(joinPoint, annotation);
            }
            case INFO_ABOUT_ALL_USERS -> {
                return getAuditDtoForInfoAboutAllUsers(annotation);
            }
            case INFO_ABOUT_USER_BY_ID, INFO_ABOUT_ME -> {
                return getAuditDtoForUserInfo(annotation, result);
            }
            case CREATE_USER, UPDATE_USER -> {
                return getAuditDtoForUser(annotation, result);
            }
            default -> throw new RuntimeException("Unrecognized action: " + annotation.auditedAction());
        }
    }

    private AuditDto getAuditDtoForInfoAboutAllUsers(Audited annotation) {
        UserDetailsDto userDetailsDto = (UserDetailsDto) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return createAuditDto(annotation, userDetailsDto, userDetailsDto.getId());
    }

    private AuditDto getAuditDtoForUserInfo(Audited annotation, Object result) {
        return createAuditDto(annotation, getUserDetailFromSecurityContext(), ((UserInfoDto) result).getId());
    }

    private AuditDto getAuditDtoForUser(Audited annotation, Object result) {
        return createAuditDto(annotation, getUserDetailFromSecurityContext(), ((User) result).getId());
    }

    private UserDetailsDto getUserDetailFromSecurityContext() {
        return (UserDetailsDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User", email));
    }

    private AuditDto createAuditDto(Audited annotation, Userable userable) {
        return new AuditDto().setUserAuditDto(buildUserAuditDto(userable))
                .setAction(annotation.auditedAction())
                .setEssenceType(annotation.essenceType())
                .setEssenceTypeId(userable.getId().toString());
    }

    private AuditDto createAuditDto(Audited annotation, Userable userable, UUID id) {
        return new AuditDto().setUserAuditDto(buildUserAuditDto(userable))
                .setAction(annotation.auditedAction())
                .setEssenceType(annotation.essenceType())
                .setEssenceTypeId(id.toString());
    }

    private UserAuditDto buildUserAuditDto(Userable userable) {
        return new UserAuditDto().setUserId(userable.getId())
                .setEmail(userable.getEmail())
                .setFio(userable.getFio())
                .setUserRole(userable.getRole());
    }

    private AuditDto getAuditDtoByEmail(ProceedingJoinPoint joinPoint, Audited annotation) {
        Emailable dto = (Emailable) Arrays.stream(joinPoint.getArgs()).toList().get(0);
        return createAuditDto(annotation, findByEmail(dto.getMail()));
    }
}