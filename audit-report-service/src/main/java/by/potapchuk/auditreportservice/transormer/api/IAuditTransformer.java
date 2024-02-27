package by.potapchuk.auditreportservice.transormer.api;

import by.potapchuk.auditreportservice.core.dto.AuditDto;
import by.potapchuk.auditreportservice.core.dto.AuditInfoDto;
import by.potapchuk.auditreportservice.core.entity.Audit;

public interface IAuditTransformer {

    AuditInfoDto transformAuditInfoDtoFromEntity(Audit audit);

    AuditDto transformAuditDtoFromEntity(Audit audit);

    Audit transformEntityFromAuditDto(AuditDto auditDto);
}
