package by.potapchuk.auditreportservice.aop;


import by.potapchuk.auditreportservice.core.dto.AuditDto;
import by.potapchuk.auditreportservice.core.dto.UserActionAuditParamDto;
import by.potapchuk.auditreportservice.core.dto.UserAuditDto;
import by.potapchuk.auditreportservice.core.dto.UserDetailsDto;
import by.potapchuk.auditreportservice.service.api.IAuditService;
import by.potapchuk.auditreportservice.service.jwt.JwtHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
public class AuditedAspect {

    private final IAuditService IAuditService;
    private final JwtHandler jwtHandler;

    public AuditedAspect(IAuditService IAuditService, JwtHandler jwtHandler) {
        this.IAuditService = IAuditService;
        this.jwtHandler = jwtHandler;
    }

    @Around("@annotation(Audited)")
    public Object checkActivation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Audited annotation = signature.getMethod().getAnnotation(Audited.class);
        Object result = joinPoint.proceed();
        AuditDto auditDto = buildAuditDto(joinPoint, annotation, result).setId(UUID.randomUUID()).setCreationDate(LocalDateTime.now());
        IAuditService.saveAction(auditDto);
        return result;
    }

    private AuditDto buildAuditDto(ProceedingJoinPoint joinPoint, Audited annotation, Object result) {
        switch (annotation.auditedAction()) {
            case INFO_ABOUT_ALL_AUDITS, INFO_ABOUT_ALL_REPORTS -> {
                return getAuditDtoForInfoAboutAllReportsAndAudits(annotation);
            }
            case INFO_ABOUT_AUDIT_BY_ID, INFO_ABOUT_ACCESS_REPORT, SAVE_REPORT -> {
                return getAuditDtoForAuditAndReportInfo(annotation, joinPoint);
            }
            case CREATE_REPORT -> {
                return getAuditDtoForCreateReport(annotation, joinPoint);
            }
            default -> throw new RuntimeException("Unrecognized action: " + annotation.auditedAction());
        }
    }

    private AuditDto getAuditDtoForInfoAboutAllReportsAndAudits(Audited annotation) {
        UserDetailsDto userDetailsDto = getUserDetailFromSecurityContext();
        return createAuditDto(annotation, userDetailsDto, userDetailsDto.getId());
    }

    private AuditDto getAuditDtoForAuditAndReportInfo(Audited annotation, ProceedingJoinPoint joinPoint) {
        return createAuditDto(annotation,
                getUserDetailFromSecurityContext(),
                UUID.fromString(Arrays.stream(joinPoint.getArgs()).toList().get(0).toString()));
    }

    private AuditDto getAuditDtoForCreateReport(Audited annotation, ProceedingJoinPoint joinPoint){
        UserActionAuditParamDto param =(UserActionAuditParamDto) Arrays.stream(joinPoint.getArgs()).toList().get(1);
        return createAuditDto(
                annotation,
                getUserDetailFromSecurityContext(),
                UUID.fromString(param.getUserId())
        );
    }

    private AuditDto createAuditDto(Audited annotation, UserDetailsDto userDetailsDto, UUID id) {
        return new AuditDto().setUserAuditDto(buildUserAuditDto(userDetailsDto))
                .setAction(annotation.auditedAction())
                .setEssenceType(annotation.essenceType())
                .setEssenceTypeId(id.toString());
    }

    private UserAuditDto buildUserAuditDto(UserDetailsDto userDetailsDto) {
        return new UserAuditDto().setUserId(userDetailsDto.getId())
                .setEmail(userDetailsDto.getEmail())
                .setFio(userDetailsDto.getFio())
                .setUserRole(userDetailsDto.getRole());
    }

    private UserDetailsDto getUserDetailFromSecurityContext() {
        return (UserDetailsDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}