package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.ModelPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.Model;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.ModelEntity;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.ModelNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper.ModelPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository.SpringModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ModelPersistenceAdapter implements ModelPersistencePort {
    private final SpringModelRepository springModelRepository;
    private final ModelPersistenceMapper modelPersistenceMapper;

    @Autowired
    public ModelPersistenceAdapter(SpringModelRepository springModelRepository,
                                    ModelPersistenceMapper modelPersistenceMapper) {
        this.springModelRepository = springModelRepository;
        this.modelPersistenceMapper = modelPersistenceMapper;
    }

    @Override
    public List<Model> findAll() {
        List<ModelEntity> entities = springModelRepository.findAll();
        return modelPersistenceMapper.toModelList(entities);
    }

    @Override
    public Optional<Model> findById(String id) {
        UUID uuid = UUID.fromString(id);
        return springModelRepository.findById(uuid)
                .map(modelPersistenceMapper::toModel);
    }

    @Override
    public Model save(Model model) {
        ModelEntity entity = modelPersistenceMapper.toModelEntity(model);
        ModelEntity saved = springModelRepository.save(entity);
        return modelPersistenceMapper.toModel(saved);
    }

    @Override
    public Model update(String id, Model model) {
        UUID uuid = UUID.fromString(id);
        ModelEntity existing = springModelRepository.findById(uuid)
                .orElseThrow(() -> new ModelNotFoundException(id));
        modelPersistenceMapper.updateEntityFromDomain(model, existing);
        ModelEntity saved = springModelRepository.save(existing);
        return modelPersistenceMapper.toModel(saved);
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (!springModelRepository.existsById(uuid)) {
            throw new ModelNotFoundException(id);
        }
        springModelRepository.deleteById(uuid);
    }
}
