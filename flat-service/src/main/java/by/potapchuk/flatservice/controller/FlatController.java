package by.potapchuk.flatservice.controller;

import by.potapchuk.flatservice.core.dto.FlatFilter;
import by.potapchuk.flatservice.core.dto.FlatWriteDto;
import by.potapchuk.flatservice.core.dto.PageOfFlatDto;
import by.potapchuk.flatservice.service.api.FlatCrudService;
import by.potapchuk.flatservice.transformer.api.PageTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/flats")
public class FlatController {

    private final FlatCrudService flatService;
    private final PageTransformer pageTransformer;

    public FlatController(FlatCrudService flatService, PageTransformer pageTransformer) {
        this.flatService = flatService;
        this.pageTransformer = pageTransformer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void saveFlat(@RequestBody FlatWriteDto flatWriteDto) {
        flatService.createFlat(flatWriteDto);
    }

    @GetMapping
    PageOfFlatDto getAllFlatByFilter(@RequestParam(name = "price_from", required = false) Integer priceFrom,
                                     @RequestParam(name = "price_to", required = false) Integer priceTo,
                                     @RequestParam(name = "bedrooms_from", required = false) Integer bedroomsFrom,
                                     @RequestParam(name = "bedrooms_to", required = false) Integer bedroomsTo,
                                     @RequestParam(name = "area_from", required = false) Integer areaFrom,
                                     @RequestParam(name = "area_to", required = false) Integer areaTo,
                                     @RequestParam(name = "floors", required = false) Integer[] floors,
                                     @RequestParam(name = "photo", required = false) Boolean photo,
                                     @RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        FlatFilter flatFilter = new FlatFilter().setPriceFrom(priceFrom)
                .setPriceTo(priceTo)
                .setBedroomsFrom(bedroomsFrom)
                .setBedroomsTo(bedroomsTo)
                .setAreaFrom(areaFrom)
                .setAreaTo(areaTo)
                .setFloors(floors)
                .setPhoto(photo);
        return pageTransformer.transformPageOfFlatDtoFromPage(flatService.getAllFlats(flatFilter, pageable));
    }
}
