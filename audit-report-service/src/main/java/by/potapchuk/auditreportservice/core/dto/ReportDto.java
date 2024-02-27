package by.potapchuk.auditreportservice.core.dto;

import by.potapchuk.auditreportservice.core.entity.ReportStatus;
import by.potapchuk.auditreportservice.core.entity.ReportType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ReportDto {

    @JsonProperty("uuid")
    private UUID id;

    @JsonProperty("dt_create")
    private Long creationDate;

    @JsonProperty("dt_update")
    private Long updateDate;

    private ReportStatus status;

    private ReportType type;

    private String description;

    private UserActionAuditParamDto params;
}
