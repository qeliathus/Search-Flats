package by.potapchuk.auditreportservice.service;

import by.potapchuk.auditreportservice.aop.Audited;
import by.potapchuk.auditreportservice.core.dto.ReportDto;
import by.potapchuk.auditreportservice.core.dto.UserActionAuditParamDto;
import by.potapchuk.auditreportservice.core.entity.Audit;
import by.potapchuk.auditreportservice.core.entity.Report;
import by.potapchuk.auditreportservice.core.entity.ReportStatus;
import by.potapchuk.auditreportservice.core.entity.ReportType;
import by.potapchuk.auditreportservice.core.exception.EntityNotFoundException;
import by.potapchuk.auditreportservice.repository.AuditRepository;
import by.potapchuk.auditreportservice.repository.ReportRepository;
import by.potapchuk.auditreportservice.service.api.IFileGenerator;
import by.potapchuk.auditreportservice.service.api.IReportService;
import by.potapchuk.auditreportservice.transormer.api.IReportTransformer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static by.potapchuk.auditreportservice.core.entity.AuditedAction.CREATE_REPORT;
import static by.potapchuk.auditreportservice.core.entity.AuditedAction.INFO_ABOUT_ACCESS_REPORT;
import static by.potapchuk.auditreportservice.core.entity.AuditedAction.INFO_ABOUT_ALL_REPORTS;
import static by.potapchuk.auditreportservice.core.entity.AuditedAction.SAVE_REPORT;
import static by.potapchuk.auditreportservice.core.entity.EssenceType.REPORT;
import static by.potapchuk.auditreportservice.core.entity.ReportStatus.DONE;
import static by.potapchuk.auditreportservice.core.entity.ReportStatus.ERROR;

@Slf4j
@Service
public class ReportService implements IReportService {

    private static final String FILE_DIRECTORY = ".";

    private final ReportRepository reportRepository;
    private final AuditRepository auditRepository;
    private final IReportTransformer IReportTransformer;
    private final IFileGenerator IFileGenerator;

    public ReportService(
            ReportRepository reportRepository,
            AuditRepository auditRepository,
            IReportTransformer IReportTransformer,
            @Qualifier("excel-file-generator") IFileGenerator IFileGenerator
    ) {
        this.reportRepository = reportRepository;
        this.auditRepository = auditRepository;
        this.IReportTransformer = IReportTransformer;
        this.IFileGenerator = IFileGenerator;
    }

    @Override
    @Async
    @Audited(auditedAction = CREATE_REPORT, essenceType = REPORT)
    public void createReport(ReportType type, UserActionAuditParamDto paramDto) {
//        if (!reportRepository.existsByUserId(paramDto.getUserId())){
//            throw new EntityNotFoundException("user", UUID.fromString(paramDto.getUserId()));
//        }
        Report reportEntityForSave = UserActionAuditParamDto.toEntity(type, paramDto);
        Report savedReport = reportRepository.saveAndFlush(reportEntityForSave);

        List<Audit> audits = auditRepository.findAllByParam(
                UUID.fromString(paramDto.getUserId()),
                LocalDateTime.of(paramDto.getFrom(), LocalTime.of(0,0)),
                LocalDateTime.of(paramDto.getTo(), LocalTime.of(0,0))
        );

        try {
            IFileGenerator.generateFile(audits, savedReport.getId().toString());
            savedReport.setStatus(DONE);
        } catch (Exception exception) {
            log.error("Error while generating report" + exception);
            savedReport.setStatus(ERROR);
        }
        reportRepository.save(savedReport);
        log.info("Saved report with status " + savedReport.getStatus());
    }

    @Override
    @Audited(auditedAction = INFO_ABOUT_ALL_REPORTS, essenceType = REPORT)
    public Page<ReportDto> getAllReports(Pageable pageable) {
        Page<Report> pageEntity = reportRepository.findAll(pageable);
        List<ReportDto> reportDtoList = pageEntity.stream()
                .map(IReportTransformer::transformReportDtoFromEntity)
                .toList();
        return new PageImpl<ReportDto>(reportDtoList, pageable, pageEntity.getTotalElements());
    }

    @Override
    @Audited(auditedAction = SAVE_REPORT, essenceType = REPORT)
    public ResponseEntity<String> saveFileByName(String uuid) throws IOException {
        String fileName = uuid + ".xlsx";
        Path filePath = Paths.get(FILE_DIRECTORY).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            byte[] fileContent = Files.readAllBytes(filePath);
            String base64Encoded = Base64.getEncoder().encodeToString(fileContent);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.add(
                    HttpHeaders.CONTENT_TYPE,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(base64Encoded);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Audited(auditedAction = INFO_ABOUT_ACCESS_REPORT, essenceType = REPORT)
    public ReportStatus getStatusById(String id) {
        try {
            ReportStatus status = ReportStatus.valueOf(reportRepository.getStatusById(UUID.fromString(id)));
            return status;
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new EntityNotFoundException("report", UUID.fromString(id));
        }
    }
}
