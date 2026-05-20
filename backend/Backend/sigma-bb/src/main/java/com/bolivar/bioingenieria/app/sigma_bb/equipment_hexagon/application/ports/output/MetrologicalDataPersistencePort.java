package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;

import java.util.List;
import java.util.Optional;

public interface MetrologicalDataPersistencePort {
    List<MetrologicalData> findAll();
    Optional<MetrologicalData> findById(String id);
    MetrologicalData save(MetrologicalData metrologicalData);
    MetrologicalData update(String id, MetrologicalData metrologicalData);
    void delete(String id);
}
