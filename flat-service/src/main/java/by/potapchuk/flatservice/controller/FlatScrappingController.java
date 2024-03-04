package by.potapchuk.flatservice.controller;

import by.potapchuk.flatservice.service.api.IFlatParsingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/flats-scrapping")
public class FlatScrappingController {

    private final IFlatParsingService IFlatParsingService;

    public FlatScrappingController(IFlatParsingService IFlatParsingService) {
        this.IFlatParsingService = IFlatParsingService;
    }

    @PostMapping("/rent")
    public void saveRentFlat() {
        IFlatParsingService.saveRentFlatsFromParsing();
    }

    @PostMapping("/sale")
    public void saveSaleFlat() {
        IFlatParsingService.saveSaleFlatsFromParsing();
    }
}