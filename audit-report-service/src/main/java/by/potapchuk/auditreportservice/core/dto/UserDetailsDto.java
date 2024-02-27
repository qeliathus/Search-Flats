package by.potapchuk.auditreportservice.core.dto;

import by.potapchuk.auditreportservice.core.entity.UserRole;
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
public class UserDetailsDto {

    private UUID id;

    private String email;

    private String fio;

    private UserRole role;
}
