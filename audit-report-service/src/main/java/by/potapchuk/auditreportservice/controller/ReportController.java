package by.potapchuk.auditreportservice.controller;

import by.potapchuk.auditreportservice.core.dto.PageOfReportDto;
import by.potapchuk.auditreportservice.core.dto.UserActionAuditParamDto;
import by.potapchuk.auditreportservice.core.entity.ReportType;
import by.potapchuk.auditreportservice.service.api.IReportService;
import by.potapchuk.auditreportservice.transormer.api.IPageTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/report")
public class ReportController {

    private final IReportService IReportService;
    private final IPageTransformer IPageTransformer;

    public ReportController(IReportService IReportService, IPageTransformer IPageTransformer) {
        this.IReportService = IReportService;
        this.IPageTransformer = IPageTransformer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{type}")
    public void startReport(@PathVariable ReportType type,
                            @Validated @RequestBody UserActionAuditParamDto paramDto) {
        IReportService.createReport(type, paramDto);
    }

    @GetMapping
    public PageOfReportDto getListReports(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return IPageTransformer.transformPageOfReportDtoFromPage(IReportService.getAllReports(pageable));
    }

    @GetMapping(value = "/{UUID}/export")
    public ResponseEntity<String> saveFile(@PathVariable (name = "UUID") String uuid) throws IOException{
        return IReportService.saveFileByName(uuid);
    }

    @RequestMapping(method = RequestMethod.HEAD, value = "/{uuid}/export")
    public ResponseEntity getReportStatus(@PathVariable String uuid) {
        return switch (IReportService.getStatusById(uuid)) {
            case DONE -> ResponseEntity.status(200).build();
            case ERROR, LOADED, PROGRESS -> ResponseEntity.status(505).build();
        };
    }
}
