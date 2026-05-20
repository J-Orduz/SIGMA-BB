package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringBrandRepository extends JpaRepository<BrandEntity, UUID> {
}
