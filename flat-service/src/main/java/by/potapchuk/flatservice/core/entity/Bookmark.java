package by.potapchuk.flatservice.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(schema = "config", name = "bookmarks")
public class Bookmark extends BaseEntity {

    private UUID flatId;

    private UUID userId;


    public Bookmark() {
    }

    public Bookmark(UUID flatId, UUID userId) {
        this.flatId = flatId;
        this.userId = userId;
    }

    public UUID getFlatId() {
        return flatId;
    }

    public void setFlatId(UUID flatId) {
        this.flatId = flatId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}