package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.Model;

import java.util.List;
import java.util.Optional;

public interface ModelPersistencePort {
    List<Model> findAll();
    Optional<Model> findById(String id);
    Model save(Model model);
    Model update(String id, Model model);
    void delete(String id);
}
