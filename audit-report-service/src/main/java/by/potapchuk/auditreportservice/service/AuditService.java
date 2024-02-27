package by.potapchuk.auditreportservice.service;

import by.potapchuk.auditreportservice.aop.Audited;
import by.potapchuk.auditreportservice.core.dto.AuditDto;
import by.potapchuk.auditreportservice.core.dto.AuditInfoDto;
import by.potapchuk.auditreportservice.core.entity.Audit;
import by.potapchuk.auditreportservice.core.exception.EntityNotFoundException;
import by.potapchuk.auditreportservice.repository.AuditRepository;
import by.potapchuk.auditreportservice.service.api.IAuditService;
import by.potapchuk.auditreportservice.transormer.api.IAuditTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static by.potapchuk.auditreportservice.core.entity.AuditedAction.INFO_ABOUT_ALL_AUDITS;
import static by.potapchuk.auditreportservice.core.entity.AuditedAction.INFO_ABOUT_AUDIT_BY_ID;
import static by.potapchuk.auditreportservice.core.entity.EssenceType.AUDIT;

@Service
public class AuditService implements IAuditService {

    private final AuditRepository auditRepository;
    private final IAuditTransformer IAuditTransformer;

    public AuditService(AuditRepository auditRepository, IAuditTransformer IAuditTransformer) {
        this.auditRepository = auditRepository;
        this.IAuditTransformer = IAuditTransformer;
    }

    @Override
    @Audited(auditedAction = INFO_ABOUT_ALL_AUDITS, essenceType = AUDIT)
    public Page<AuditInfoDto> getAllAudits(Pageable pageable) {
        Page<Audit> pageEntity = auditRepository.findAll(pageable);
        List<AuditInfoDto> auditInfoDtoList = pageEntity.stream()
                .map(IAuditTransformer::transformAuditInfoDtoFromEntity)
                .toList();
        return new PageImpl<AuditInfoDto>(auditInfoDtoList, pageable, pageEntity.getTotalElements());
    }

    @Override
    @Audited(auditedAction = INFO_ABOUT_AUDIT_BY_ID, essenceType = AUDIT)
    public AuditInfoDto findAuditById(UUID id) {
        Audit auditById = auditRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Audit", id));
        return IAuditTransformer.transformAuditInfoDtoFromEntity(auditById);
    }

    @Override
    public AuditDto saveAction(AuditDto auditDto) {
        Audit entity = auditRepository.save(IAuditTransformer.transformEntityFromAuditDto(auditDto));
        return IAuditTransformer.transformAuditDtoFromEntity(entity);
    }
}
