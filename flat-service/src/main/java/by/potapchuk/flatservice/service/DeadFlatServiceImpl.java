package by.potapchuk.flatservice.service;

import by.potapchuk.flatservice.core.entity.DeadFlat;
import by.potapchuk.flatservice.repository.api.DeadFlatRepository;
import by.potapchuk.flatservice.service.api.DeadFlatService;
import org.springframework.stereotype.Component;

@Component
public class DeadFlatServiceImpl implements DeadFlatService {

    private final DeadFlatRepository deadFlatRepository;

    public DeadFlatServiceImpl(DeadFlatRepository deadFlatRepository) {
        this.deadFlatRepository = deadFlatRepository;
    }

    @Override
    public void saveDeadFlat(DeadFlat deadFlat){
        deadFlatRepository.save(deadFlat);
    }
}
