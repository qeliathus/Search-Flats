package by.potapchuk.flatservice.service.api;

import by.potapchuk.flatservice.core.dto.FlatFilter;
import by.potapchuk.flatservice.core.dto.FlatInfoDto;
import by.potapchuk.flatservice.core.dto.FlatWriteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlatCrudService {

    void createFlat(FlatWriteDto flatWriteDto);

    Page<FlatInfoDto> getAllFlats(FlatFilter flatFilter, Pageable pageable);
}
