package by.potapchuk.auditreportservice.service.api;

import by.potapchuk.auditreportservice.core.dto.AuditDto;
import by.potapchuk.auditreportservice.core.dto.AuditInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IAuditService {

    Page<AuditInfoDto> getAllAudits(Pageable pageable);

    AuditInfoDto findAuditById(UUID id);

    AuditDto saveAction(AuditDto auditDto);
}