package by.potapchuk.auditreportservice.core.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, UUID id) {
        super(entityName + " not found by id " + id);
    }
}
