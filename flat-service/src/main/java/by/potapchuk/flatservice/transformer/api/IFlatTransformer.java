package by.potapchuk.flatservice.transformer.api;

import by.potapchuk.flatservice.core.dto.FlatInfoDto;
import by.potapchuk.flatservice.core.dto.FlatWriteDto;
import by.potapchuk.flatservice.core.entity.Flat;

public interface IFlatTransformer {

    FlatInfoDto transformFlatInfoDtoFromEntity(Flat flat);

    Flat transformEntityFromFlatWriteDto(FlatWriteDto flatInfoDto);
}
