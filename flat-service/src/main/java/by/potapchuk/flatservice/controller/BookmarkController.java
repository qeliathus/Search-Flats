package by.potapchuk.flatservice.controller;

import by.potapchuk.flatservice.core.dto.BookmarkDto;
import by.potapchuk.flatservice.core.dto.PageOfFlatDto;
import by.potapchuk.flatservice.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/flats/{uuid}/bookmark")
    public ResponseEntity<Void> addBookmark(@PathVariable("uuid") UUID flatId, @RequestHeader("Authorization") String token) {
        bookmarkService.addBookmark(new BookmarkDto(flatId, token));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/flats/{uuid}/bookmark")
    public ResponseEntity<Void> deleteBookmark(@PathVariable("uuid") UUID flatId, @RequestHeader("Authorization") String token) {
        bookmarkService.deleteBookmark(new BookmarkDto(flatId, token));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookmark")
    public ResponseEntity<PageOfFlatDto> viewBookmarkedFlats(@RequestHeader("Authorization") String token,
                                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                 @RequestParam(value = "size", defaultValue = "20") Integer size) {
        PageOfFlatDto pageOfFlatDto = bookmarkService.viewBookmarkedFlats(token, PageRequest.of(page - 1, size));
        return ResponseEntity.ok(pageOfFlatDto);
    }

}
