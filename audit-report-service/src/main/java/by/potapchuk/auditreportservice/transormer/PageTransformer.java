package by.potapchuk.auditreportservice.transormer;

import by.potapchuk.auditreportservice.core.dto.AuditInfoDto;
import by.potapchuk.auditreportservice.core.dto.PageOfAuditInfoDto;
import by.potapchuk.auditreportservice.core.dto.PageOfReportDto;
import by.potapchuk.auditreportservice.core.dto.ReportDto;
import by.potapchuk.auditreportservice.transormer.api.IPageTransformer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageTransformer implements IPageTransformer {

    @Override
    public PageOfAuditInfoDto transformPageOfAuditInfoDtoFromPage(Page<AuditInfoDto> page) {
        return (PageOfAuditInfoDto) new PageOfAuditInfoDto()
                .setContent(page.getContent())
                .setNumber(page.getNumber())
                .setSize(page.getSize())
                .setTotalPages(page.getTotalPages())
                .setTotalElements(page.getTotalElements())
                .setFirst(page.isFirst())
                .setNumberOfElements(page.getNumberOfElements())
                .setLast(page.isLast());
    }

    @Override
    public PageOfReportDto transformPageOfReportDtoFromPage(Page<ReportDto> page) {
        return (PageOfReportDto) new PageOfReportDto()
                .setContent(page.getContent())
                .setNumber(page.getNumber())
                .setSize(page.getSize())
                .setTotalPages(page.getTotalPages())
                .setTotalElements(page.getTotalElements())
                .setFirst(page.isFirst())
                .setNumberOfElements(page.getNumberOfElements())
                .setLast(page.isLast());
    }
}
