package by.potapchuk.auditreportservice.core.dto;

import by.potapchuk.auditreportservice.core.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class UserAuditDto {

    @JsonProperty("uuid")
    private UUID userId;

    @JsonProperty("mail")
    private String email;

    private String fio;

    @JsonProperty("role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}