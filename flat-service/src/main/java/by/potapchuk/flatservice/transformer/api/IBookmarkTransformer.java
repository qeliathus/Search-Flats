package by.potapchuk.flatservice.transformer.api;

import by.potapchuk.flatservice.core.dto.FlatInfoDto;
import by.potapchuk.flatservice.core.dto.PageOfFlatDto;
import by.potapchuk.flatservice.core.entity.Bookmark;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBookmarkTransformer {
    PageOfFlatDto transformPageOfFlatDtoFromEntity(Page<Bookmark> bookmarkPage, List<FlatInfoDto> flatInfoDtos);
}
