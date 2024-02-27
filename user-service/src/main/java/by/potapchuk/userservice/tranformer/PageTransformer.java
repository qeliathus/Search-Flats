package by.potapchuk.userservice.tranformer;

import by.potapchuk.userservice.core.dto.PageDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import by.potapchuk.userservice.tranformer.api.IPageTransformer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageTransformer implements IPageTransformer {

    @Override
    public PageDto<UserInfoDto> transformPageDtoFromPage(Page<UserInfoDto> page) {
        return new PageDto<UserInfoDto>().setNumber(page.getNumber())
                .setSize(page.getSize())
                .setTotalPages(page.getTotalPages())
                .setTotalElements(page.getTotalElements())
                .setFirst(page.isFirst())
                .setNumberOfElements(page.getNumberOfElements())
                .setLast(page.isLast())
                .setContent(page.getContent());
    }

}
