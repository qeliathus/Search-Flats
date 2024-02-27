package by.potapchuk.auditreportservice.transormer.api;

import by.potapchuk.auditreportservice.core.dto.AuditInfoDto;
import by.potapchuk.auditreportservice.core.dto.PageOfAuditInfoDto;
import by.potapchuk.auditreportservice.core.dto.PageOfReportDto;
import by.potapchuk.auditreportservice.core.dto.ReportDto;
import org.springframework.data.domain.Page;

public interface IPageTransformer {

    PageOfAuditInfoDto transformPageOfAuditInfoDtoFromPage(Page<AuditInfoDto> page);

    PageOfReportDto transformPageOfReportDtoFromPage(Page<ReportDto> page);

}
