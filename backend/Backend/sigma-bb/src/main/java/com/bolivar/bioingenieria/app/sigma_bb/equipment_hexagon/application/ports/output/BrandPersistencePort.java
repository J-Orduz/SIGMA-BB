package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandPersistencePort {
    List<Brand> findAll();
    Optional<Brand> findById(String id);
    Brand save(Brand brand);
    Brand update(String id, Brand brand);
    void delete(String id);
}
