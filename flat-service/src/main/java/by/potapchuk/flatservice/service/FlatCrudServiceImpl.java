package by.potapchuk.flatservice.service;

import by.potapchuk.flatservice.core.dto.FlatFilter;
import by.potapchuk.flatservice.core.dto.FlatInfoDto;
import by.potapchuk.flatservice.core.dto.FlatWriteDto;
import by.potapchuk.flatservice.core.entity.Flat;
import by.potapchuk.flatservice.repository.api.FlatRepository;
import by.potapchuk.flatservice.service.api.FlatCrudService;
import by.potapchuk.flatservice.transformer.api.FlatTransformer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FlatCrudServiceImpl implements FlatCrudService {

    private final FlatRepository flatRepository;
    private final FlatTransformer flatTransformer;

    public FlatCrudServiceImpl(
            FlatRepository flatRepository,
            FlatTransformer flatTransformer
    ) {
        this.flatRepository = flatRepository;
        this.flatTransformer = flatTransformer;
    }


    @Override
    public void createFlat(FlatWriteDto flatWriteDto) {
        flatRepository.save(flatTransformer.transformEntityFromFlatWriteDto(flatWriteDto));
    }

    @Override
    public Page<FlatInfoDto> getAllFlats(FlatFilter flatFilter, Pageable pageable) {
        return flatRepository.findAll(buildQuery(flatFilter), pageable).map(flatTransformer::transformFlatInfoDtoFromEntity);
    }

    private Specification<Flat> buildQuery(FlatFilter flatFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (flatFilter.getPriceFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), flatFilter.getPriceFrom()));
            }
            if (flatFilter.getPriceTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), flatFilter.getPriceTo()));
            }
            if (flatFilter.getBedroomsFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bedrooms"), flatFilter.getBedroomsFrom()));
            }
            if (flatFilter.getBedroomsTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("bedrooms"), flatFilter.getBedroomsTo()));
            }
            if (flatFilter.getAreaFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("area"), flatFilter.getAreaFrom()));
            }
            if (flatFilter.getAreaTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("area"), flatFilter.getAreaTo()));
            }
            if (flatFilter.getFloors() != null && flatFilter.getFloors().length > 0) {
                predicates.add(root.get("floor").in(flatFilter.getFloors()));
            }
            if (flatFilter.getPhoto() != null && flatFilter.getPhoto()) {
                predicates.add(criteriaBuilder.isNotNull(root.get("photoUrls")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
