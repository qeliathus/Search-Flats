package by.potapchuk.userservice.core.dto;

import by.potapchuk.userservice.core.entity.UserStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserQueryDto {

    @NotNull
    @Size(min = 6, max = 12)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserStatus status;
}
