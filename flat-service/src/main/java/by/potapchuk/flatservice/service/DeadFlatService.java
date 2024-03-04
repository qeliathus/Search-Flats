package by.potapchuk.flatservice.service;

import by.potapchuk.flatservice.core.entity.DeadFlat;
import by.potapchuk.flatservice.repository.api.DeadFlatRepository;
import by.potapchuk.flatservice.service.api.IDeadFlatService;
import org.springframework.stereotype.Component;

@Component
public class DeadFlatService implements IDeadFlatService {

    private final DeadFlatRepository deadFlatRepository;

    public DeadFlatService(DeadFlatRepository deadFlatRepository) {
        this.deadFlatRepository = deadFlatRepository;
    }

    @Override
    public void saveDeadFlat(DeadFlat deadFlat){
        deadFlatRepository.save(deadFlat);
    }
}
