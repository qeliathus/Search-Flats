package by.potapchuk.auditreportservice.service.api;

import by.potapchuk.auditreportservice.core.dto.ReportDto;
import by.potapchuk.auditreportservice.core.dto.UserActionAuditParamDto;
import by.potapchuk.auditreportservice.core.entity.ReportStatus;
import by.potapchuk.auditreportservice.core.entity.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IReportService {

    void createReport(ReportType type, UserActionAuditParamDto paramDto);

    Page<ReportDto> getAllReports(Pageable pageable);

    ResponseEntity<String> saveFileByName(String uuid) throws IOException;

    ReportStatus getStatusById(String id);
}
