package by.potapchuk.userservice.aop;

import by.potapchuk.userservice.core.entity.AuditedAction;
import by.potapchuk.userservice.core.entity.EssenceType;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audited {

    AuditedAction auditedAction();

    EssenceType essenceType();
}
