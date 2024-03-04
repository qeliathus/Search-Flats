package by.potapchuk.flatservice.service;

import by.potapchuk.flatservice.core.dto.BookmarkDto;
import by.potapchuk.flatservice.core.dto.FlatInfoDto;
import by.potapchuk.flatservice.core.dto.PageOfFlatDto;
import by.potapchuk.flatservice.core.dto.UserDetailsDto;
import by.potapchuk.flatservice.core.entity.Bookmark;
import by.potapchuk.flatservice.core.exceptions.ValidationException;
import by.potapchuk.flatservice.repository.api.BookmarkRepository;
import by.potapchuk.flatservice.transformer.api.IBookmarkTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    @Value("${app.jwt.secret}")
    private String secret;
    private final ObjectMapper objectMapper;
    private final BookmarkRepository bookmarkRepository;
    private final FlatCrudService flatCrudService;
    private final IBookmarkTransformer bookmarkTransformer;

    public BookmarkService(ObjectMapper objectMapper, BookmarkRepository bookmarkRepository, FlatCrudService flatCrudService, IBookmarkTransformer bookmarkTransformer) {
        this.objectMapper = objectMapper;
        this.bookmarkRepository = bookmarkRepository;
        this.flatCrudService = flatCrudService;
        this.bookmarkTransformer = bookmarkTransformer;
    }

    public void addBookmark(BookmarkDto bookmarkDto) {
        UUID userId = getUserId(bookmarkDto.getToken());
        UUID flatId = bookmarkDto.getFlatId();
        if (bookmarkRepository.existsByFlatIdAndUserId(flatId, userId)) {
            throw new ValidationException("Квартира уже добавлена в закладку");
        }
        bookmarkRepository.save(new Bookmark(flatId, userId));
    }

    @Transactional
    public void deleteBookmark(BookmarkDto bookmarkDto) {
        UUID userId = getUserId(bookmarkDto.getToken());
        bookmarkRepository.deleteByFlatIdAndUserId(bookmarkDto.getFlatId(), userId);
    }

    public PageOfFlatDto viewBookmarkedFlats(String token, Pageable pageable) {
        UUID userId = getUserId(token);
        Page<Bookmark> bookmarkPage = bookmarkRepository.findAllByUserId(userId, pageable);
        List<FlatInfoDto> flatsInfo = bookmarkPage.get()
                .map(bookmark -> flatCrudService.getFlatById(bookmark.getFlatId()))
                .collect(Collectors.toList());
        return bookmarkTransformer.transformPageOfFlatDtoFromEntity(bookmarkPage, flatsInfo);
    }

    private UUID getUserId(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        String userDetailsJson = claims.getBody().getSubject();
        UserDetailsDto userDetailsDto = convertJsonToDto(userDetailsJson);
        return userDetailsDto.getId();
    }

    private UserDetailsDto convertJsonToDto(String userDetailsJson) {
        try {
            return objectMapper.readValue(userDetailsJson, UserDetailsDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ValidationException("Запрос содержит некорректные данные. Измените запрос и отправьте его ещё раз");
        }
    }

}
