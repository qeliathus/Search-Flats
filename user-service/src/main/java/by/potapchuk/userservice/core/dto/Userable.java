package by.potapchuk.userservice.core.dto;

import by.potapchuk.userservice.core.entity.UserRole;

import java.util.UUID;

public interface Userable extends Identifiable {

    UUID getId();

    String getEmail();

    String getFio();

    UserRole getRole();
}
