package by.potapchuk.flatservice.transformer;

import by.potapchuk.flatservice.core.dto.FlatInfoDto;
import by.potapchuk.flatservice.core.dto.PageOfFlatDto;
import by.potapchuk.flatservice.transformer.api.PageTransformer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageTransformerImpl implements PageTransformer {

    @Override
    public PageOfFlatDto transformPageOfFlatDtoFromPage(Page<FlatInfoDto> page) {
        return (PageOfFlatDto) new PageOfFlatDto()
                .setContent(page.getContent())
                .setNumber(page.getNumber())
                .setSize(page.getSize())
                .setTotalPages(page.getTotalPages())
                .setTotalElements(page.getTotalElements())
                .setFirst(page.isFirst())
                .setNumberOfElements(page.getNumberOfElements())
                .setLast(page.isLast());
    }
}
