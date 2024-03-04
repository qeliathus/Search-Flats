package by.potapchuk.flatservice.transformer;

import by.potapchuk.flatservice.core.dto.FlatInfoDto;
import by.potapchuk.flatservice.core.dto.PageOfFlatDto;
import by.potapchuk.flatservice.core.entity.Bookmark;
import by.potapchuk.flatservice.transformer.api.IBookmarkTransformer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookmarkTransformer implements IBookmarkTransformer {

    @Override
    public PageOfFlatDto transformPageOfFlatDtoFromEntity(Page<Bookmark> bookmarkPage, List<FlatInfoDto> flatInfoDtos) {
        return (PageOfFlatDto) new PageOfFlatDto()
                .setContent(flatInfoDtos)
                .setNumber(bookmarkPage.getNumber())
                .setSize(bookmarkPage.getSize())
                .setTotalPages(bookmarkPage.getTotalPages())
                .setTotalElements(bookmarkPage.getTotalElements())
                .setFirst(bookmarkPage.isFirst())
                .setNumberOfElements(bookmarkPage.getNumberOfElements())
                .setLast(bookmarkPage.isLast());
    }
}
