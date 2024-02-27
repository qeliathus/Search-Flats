package by.potapchuk.flatservice.controller;

import by.potapchuk.flatservice.service.api.FlatParsingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/flats-scrapping")
public class FlatScrappingController {

    private final FlatParsingService flatParsingService;

    public FlatScrappingController(FlatParsingService flatParsingService) {
        this.flatParsingService = flatParsingService;
    }

    @PostMapping("/rent")
    public void saveRentFlat() {
        flatParsingService.saveRentFlatsFromParsing();
    }

    @PostMapping("/sale")
    public void saveSaleFlat() {
        flatParsingService.saveSaleFlatsFromParsing();
    }
}