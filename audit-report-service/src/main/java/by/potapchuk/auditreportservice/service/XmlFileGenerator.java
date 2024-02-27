package by.potapchuk.auditreportservice.service;

import by.potapchuk.auditreportservice.core.entity.Audit;
import by.potapchuk.auditreportservice.core.entity.xml.XmlAudit;
import by.potapchuk.auditreportservice.core.entity.xml.XmlAuditList;
import by.potapchuk.auditreportservice.service.api.IFileGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;

@Qualifier("xml-file-generator")
@Component
public class XmlFileGenerator implements IFileGenerator {

    private final Marshaller marshaller;

    public XmlFileGenerator(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    @Override
    public void generateFile(List<Audit> audits, String filename) throws Exception {
        List<XmlAudit> xmlAudits = audits.stream().map(XmlAudit::from).toList();
        XmlAuditList xmlAuditList = new XmlAuditList().setAudits(xmlAudits);
        marshaller.marshal(xmlAuditList, new File(filename + ".xml"));
    }
}
