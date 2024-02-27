package by.potapchuk.auditreportservice.service.api;

import by.potapchuk.auditreportservice.core.entity.Audit;

import java.util.List;

public interface IFileGenerator {

    void generateFile(List<Audit> audits, String filename) throws Exception;
}