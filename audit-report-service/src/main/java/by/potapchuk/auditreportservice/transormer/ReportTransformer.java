package by.potapchuk.auditreportservice.transormer;

import by.potapchuk.auditreportservice.core.dto.ReportDto;
import by.potapchuk.auditreportservice.core.dto.UserActionAuditParamDto;
import by.potapchuk.auditreportservice.core.entity.Report;
import by.potapchuk.auditreportservice.transormer.api.IReportTransformer;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class ReportTransformer implements IReportTransformer {

    @Override
    public ReportDto transformReportDtoFromEntity(Report report) {
        return new ReportDto().setStatus(report.getStatus())
                .setType(report.getType())
                .setDescription(report.getDescription())
                .setParams(getParamFromEntity(report))
                .setId(report.getId())
                .setCreationDate(report.getCreationDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setUpdateDate(report.getUpdateDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    private UserActionAuditParamDto getParamFromEntity(Report report) {
        return new UserActionAuditParamDto().setUserId(report.getUserId())
                .setFrom(report.getFromDate())
                .setTo(report.getToDate());
    }
}
