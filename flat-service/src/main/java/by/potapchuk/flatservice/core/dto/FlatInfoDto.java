package by.potapchuk.flatservice.core.dto;

import by.potapchuk.flatservice.core.entity.OfferType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class FlatInfoDto {

    @JsonProperty("uuid")
    private UUID id;

    @JsonProperty("dt_create")
    private Long creationDate;

    @JsonProperty("dt_update")
    private Long updatedDate;

    @JsonProperty("offer_type")
    private OfferType offerType;

    private String description;

    private Integer bedrooms;

    private Integer area;

    private Integer price;

    private Integer floor;

    @JsonProperty("photo_urls")
    private String[] photoUrls;

    @JsonProperty("original_url")
    private String originalUrl;
}
