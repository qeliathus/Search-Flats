package by.potapchuk.flatservice.transformer.api;

import by.potapchuk.flatservice.core.dto.FlatInfoDto;
import by.potapchuk.flatservice.core.dto.PageOfFlatDto;
import org.springframework.data.domain.Page;

public interface IPageTransformer {

    PageOfFlatDto transformPageOfFlatDtoFromPage(Page<FlatInfoDto> page);
}
