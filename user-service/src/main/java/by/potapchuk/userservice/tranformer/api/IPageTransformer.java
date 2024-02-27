package by.potapchuk.userservice.tranformer.api;

import by.potapchuk.userservice.core.dto.PageDto;
import by.potapchuk.userservice.core.dto.UserInfoDto;
import org.springframework.data.domain.Page;

public interface IPageTransformer {

    PageDto<UserInfoDto> transformPageDtoFromPage(Page<UserInfoDto> page);
}
