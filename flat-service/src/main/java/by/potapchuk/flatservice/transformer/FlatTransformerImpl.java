package by.potapchuk.flatservice.transformer;

import by.potapchuk.flatservice.core.dto.FlatInfoDto;
import by.potapchuk.flatservice.core.dto.FlatWriteDto;
import by.potapchuk.flatservice.core.entity.Flat;
import by.potapchuk.flatservice.transformer.api.FlatTransformer;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class FlatTransformerImpl implements FlatTransformer {

    @Override
    public FlatInfoDto transformFlatInfoDtoFromEntity(Flat flat) {
        return new FlatInfoDto().setId(flat.getId())
                .setCreationDate(flat.getCreationDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setUpdatedDate(flat.getUpdateDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setOfferType(flat.getOfferType())
                .setDescription(flat.getDescription())
                .setBedrooms(flat.getBedrooms())
                .setArea(flat.getArea())
                .setPrice(flat.getPrice())
                .setFloor(flat.getFloor())
                .setPhotoUrls(flat.getPhotoUrls())
                .setOriginalUrl(flat.getOriginalUrl());
    }

    @Override
    public Flat transformEntityFromFlatWriteDto(FlatWriteDto flatWriteDto) {
        return new Flat().setOfferType(flatWriteDto.getOfferType())
                .setDescription(flatWriteDto.getDescription())
                .setBedrooms(flatWriteDto.getBedrooms())
                .setArea(flatWriteDto.getArea())
                .setPrice(flatWriteDto.getPrice())
                .setFloor(flatWriteDto.getFloor())
                .setPhotoUrls(flatWriteDto.getPhotoUrls())
                .setOriginalUrl(flatWriteDto.getOriginalUrl());
    }
}
