package by.potapchuk.flatservice.repository.api;

import by.potapchuk.flatservice.core.entity.DeadFlat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeadFlatRepository extends JpaRepository<DeadFlat, UUID> {
}
