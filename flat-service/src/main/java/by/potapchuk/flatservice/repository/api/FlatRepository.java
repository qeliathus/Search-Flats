package by.potapchuk.flatservice.repository.api;

import by.potapchuk.flatservice.core.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlatRepository extends JpaRepository<Flat, UUID>, JpaSpecificationExecutor<Flat> {
}