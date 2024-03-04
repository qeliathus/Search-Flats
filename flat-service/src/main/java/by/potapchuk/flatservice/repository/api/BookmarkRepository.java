package by.potapchuk.flatservice.repository.api;


import by.potapchuk.flatservice.core.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {
    void deleteByFlatIdAndUserId(UUID flatId, UUID userId);
    Page<Bookmark> findAllByUserId(UUID userId, Pageable pageable);
    Boolean existsByFlatIdAndUserId(UUID flatId, UUID userId);

}
