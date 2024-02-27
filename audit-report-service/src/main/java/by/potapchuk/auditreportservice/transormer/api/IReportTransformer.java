package by.potapchuk.auditreportservice.transormer.api;

import by.potapchuk.auditreportservice.core.dto.ReportDto;
import by.potapchuk.auditreportservice.core.entity.Report;

public interface IReportTransformer {

    ReportDto transformReportDtoFromEntity(Report report);
}
