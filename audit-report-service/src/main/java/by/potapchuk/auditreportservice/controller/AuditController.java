package by.potapchuk.auditreportservice.controller;

import by.potapchuk.auditreportservice.core.dto.AuditDto;
import by.potapchuk.auditreportservice.core.dto.AuditInfoDto;
import by.potapchuk.auditreportservice.core.dto.PageOfAuditInfoDto;
import by.potapchuk.auditreportservice.service.api.IAuditService;
import by.potapchuk.auditreportservice.transormer.api.IPageTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/audit")
public class AuditController {

    private final IAuditService IAuditService;
    private final IPageTransformer IPageTransformer;

    public AuditController(IAuditService IAuditService, IPageTransformer IPageTransformer) {
        this.IAuditService = IAuditService;
        this.IPageTransformer = IPageTransformer;
    }

    @GetMapping
    public PageOfAuditInfoDto getListAudits(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return IPageTransformer.transformPageOfAuditInfoDtoFromPage(IAuditService.getAllAudits(pageable));
    }

    @GetMapping("/{id}")
    public AuditInfoDto getAuditById(@PathVariable UUID id) {
        return IAuditService.findAuditById(id);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public AuditDto acceptRequestToCreateLog(@RequestHeader String AUTHORIZATION, @RequestBody AuditDto auditDto) {
        return IAuditService.saveAction(auditDto);
    }
}