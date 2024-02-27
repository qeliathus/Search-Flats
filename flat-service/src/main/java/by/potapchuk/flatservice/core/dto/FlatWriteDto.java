package by.potapchuk.flatservice.core.dto;

import by.potapchuk.flatservice.core.entity.OfferType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class FlatWriteDto {

    private OfferType offerType;

    private String description;

    private Integer bedrooms;

    private Integer area;

    private Integer price;

    private Integer floor;

    private String[] photoUrls;

    private String originalUrl;
}

